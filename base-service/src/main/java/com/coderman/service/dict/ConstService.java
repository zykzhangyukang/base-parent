package com.coderman.service.dict;

import com.coderman.api.anntation.ConstList;
import com.coderman.api.anntation.Constant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author coderman
 * @Title: 加载常量类
 * @date 2022/1/2519:48
 */
public class ConstService {

    /**
     * 存放常量
     */
    private final static Map<String, List<ConstItem>> constMap = new HashMap<>();

    /**
     * 读取器工厂
     */
    private final static CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();

    /**
     * 日志打印
     */
    private final static Logger logger = LoggerFactory.getLogger(ConstService.class);


    static {

        final Set<String> noAllowedConflictGroupSet = new HashSet<>();

        // 冲突记录Map, k=group, v=className
        final Map<String,Set<String>> conflictRecordMap = new HashMap<>();

        StopWatch stopWatch = new StopWatch();

        logger.info("扫描项目常量开始");
        stopWatch.start();

        // 扫描常量class
        Set<BeanDefinition> constantBeanSet = getConstantBeanSet();

        for (BeanDefinition beanDefinition : constantBeanSet) {

            try {

                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());

                if (!beanClazz.isAnnotationPresent(Constant.class)) {
                    continue;
                }

                Constant constant = beanClazz.getAnnotation(Constant.class);

                // 是否允许冲突
                boolean allowedConflict = constant.allowedConflict();


                // 获取中的属性
                Field[] declaredFields = beanClazz.getDeclaredFields();

                for (Field declaredField : declaredFields) {

                    Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
                    for (Annotation declaredAnnotation : declaredAnnotations) {

                        if (!(declaredAnnotation instanceof ConstList)) {
                            continue;
                        }


                        String group = ((ConstList) declaredAnnotation).group();
                        String name = ((ConstList) declaredAnnotation).name();


                        // 如果本项目的常量 & 不允许冲突
                        if (isInnerClass(beanDefinition) && !allowedConflict) {
                            noAllowedConflictGroupSet.add(group);
                        }


                        // 如果不是本项目的常量 & 出现冲突
                        if (!isInnerClass(beanDefinition) && constMap.containsKey(group) && noAllowedConflictGroupSet.contains(group)) {

                            logger.warn("出现冲突 class->{},group->{},name->{},不加入常量.", beanDefinition.getBeanClassName(), group, name);
                            continue;
                        }

                        List<ConstItem> constItems;

                        if (constMap.containsKey(group)) {
                            constItems = constMap.get(group);
                        } else {
                            constItems = new ArrayList<>();
                            constMap.put(group, constItems);
                        }


                        constItems.add(new ConstItem(declaredField.get(null), name));

                        // 冲突记录到map
                        Set<String> classNameSet = conflictRecordMap.get(group);

                        if(classNameSet == null){


                            classNameSet = new HashSet<>();
                        }

                        classNameSet.add(beanDefinition.getBeanClassName());

                        conflictRecordMap.put(group,classNameSet);
                        break;


                    }
                }


                logger.info("扫描常量: {}",beanDefinition.getBeanClassName());

            } catch (Exception e) {

                logger.warn("扫描常量 {} 错误,请检查常量定义修饰符.常量需要用 public static final 修饰.", beanDefinition.getBeanClassName());
            }

        }

        // 打印冲突
        for (Map.Entry<String, Set<String>> entry : conflictRecordMap.entrySet()) {

            Set<String> classNameSet = entry.getValue();
            if(classNameSet!=null && classNameSet.size() >=2){

                logger.error(MessageFormat.format("请请注意,扫描常量类class冲突,group:{},classes:{}",entry.getKey(),classNameSet.toString()));
            }

        }

        stopWatch.stop();
        logger.info("扫描常量完成,耗时:{} 毫秒", stopWatch.getTotalTimeMillis());

    }


    /**
     * 是否属于本项目的常量
     *
     * @param beanDefinition bean定义
     * @return
     */
    private static boolean isInnerClass(BeanDefinition beanDefinition) {
        return (beanDefinition.getBeanClassName() != null && beanDefinition.getBeanClassName().split("\\.").length > 3)
                && StringUtils.equals(System.getProperty("domain"), beanDefinition.getBeanClassName().split("\\.")[2]);
    }


    /**
     * 获取所有常量
     *
     * @return 所有常量
     */
    public static Map<String, List<ConstItem>> getAllConst() {
        return cloneConstMap();
    }


    /**
     * 获取所有常量集合
     *
     * @return 所有常量
     */
    public static List<ConstItems> getAllConstList() {

        List<ConstItems> list = new ArrayList<>();
        for (String key : constMap.keySet()) {

            list.add(new ConstItems(key, cloneItems(constMap.get(key))));

        }

        return list;
    }


    /**
     * 根据组名获取常量集合
     *
     * @param group 常量组
     * @return 常量集合
     */
    public static List<ConstItem> getConstList(String group){
        return cloneItems(constMap.get(group));
    }


    /**
     * 克隆对象
     *
     * @param constItems 常量项
     * @return 克隆常量
     */
    private static List<ConstItem> cloneItems(List<ConstItem> constItems) {

        List<ConstItem> newList = new ArrayList<>();
        if (CollectionUtils.isEmpty(constItems)) {

            return newList;
        }

        for (ConstItem item : constItems) {

            newList.add(new ConstItem(item.getCode(), item.getName()));
        }

        return newList;
    }

    /**
     * 克隆一个常量map
     *
     * @return 常量map
     */
    private static Map<String, List<ConstItem>> cloneConstMap() {

        Map<String, List<ConstItem>> map = new HashMap<>();

        for (Map.Entry<String, List<ConstItem>> entry : ConstService.constMap.entrySet()) {

            List<ConstItem> constItems = new ArrayList<>();
            for (ConstItem constItem : entry.getValue()) {
                constItems.add(new ConstItem(constItem.getCode(), constItem.getName()));
            }

            map.put(entry.getKey(), constItems);
        }

        return map;
    }


    /**
     * 扫描常量
     *
     * @return bean定义
     */
    private static Set<BeanDefinition> getConstantBeanSet() {

        Set<BeanDefinition> constBeanSet = new HashSet<>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources("classpath*:com/coderman/**/constant/**/*.class");

            for (Resource resource : resources) {

                if (resource.isReadable()) {

                    MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                    ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                    sbd.setResource(resource);
                    sbd.setSource(resource);
                    constBeanSet.add(sbd);
                }
            }

            return constBeanSet;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return constBeanSet;
    }

}
