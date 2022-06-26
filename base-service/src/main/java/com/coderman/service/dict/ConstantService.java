package com.coderman.service.dict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author coderman
 * @Title: 加载常量类
 * @Description: TOD
 * @date 2022/1/2519:48
 */
@Component
@Lazy(value = false)
public class ConstantService {

    // 存放常量
    private final static Map<String, List<ConstantItem>> constMap = new HashMap<>();

    // 读取器工厂
    private final static CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();

    // 日志打印
    private final static Logger logger = LoggerFactory.getLogger(ConstantService.class);

    @PostConstruct
    public void init() {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("扫描常量开始");

        // 扫描常量class
        Set<BeanDefinition> constantBeanSet = getConstantBeanSet();

        for (BeanDefinition beanDefinition : constantBeanSet) {

            try {

                // 获取中的属性
                Field[] declaredFields = Class.forName(beanDefinition.getBeanClassName()).getDeclaredFields();

                for (Field declaredField : declaredFields) {

                    Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
                    for (Annotation declaredAnnotation : declaredAnnotations) {

                        if (!(declaredAnnotation instanceof ConstantList)) {
                            continue;
                        }


                        String group = ((ConstantList) declaredAnnotation).group();
                        String name = ((ConstantList) declaredAnnotation).name();

                        List<ConstantItem> constantItems;




                        if (constMap.containsKey(group)) {
                            constantItems = constMap.get(group);
                        } else {
                            constantItems = new ArrayList<>();
                            constMap.put(group, constantItems);
                        }

                        constantItems.add(new ConstantItem(declaredField.get(null), name));
                    }
                }


                logger.info("扫描常量:{}", beanDefinition.getBeanClassName());

            } catch (ClassNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
    }



    /**
     * 获取所有常量
     *
     * @return
     */
    public static Map<String, List<ConstantItem>> getAllConst() {
        return cloneConstMap();
    }


    /**
     * 获取所有常量集合
     *
     * @return
     */
    public List<ConstantItems> getAllConstList() {

        List<ConstantItems> list = new ArrayList<>();
        for (String key : constMap.keySet()) {

            list.add(new ConstantItems(key, cloneItems(constMap.get(key))));

        }

        return list;
    }


    /**
     * 克隆对象
     *
     * @param constantItems
     * @return
     */
    private List<ConstantItem> cloneItems(List<ConstantItem> constantItems) {

        List<ConstantItem> newList = new ArrayList<>();
        if (CollectionUtils.isEmpty(constantItems)) {

            return newList;
        }

        for (ConstantItem item : constantItems) {

            newList.add(new ConstantItem(item.getCode(), item.getName()));
        }

        return newList;
    }

    /**
     * 克隆一个常量map
     *
     * @return
     */
    private static Map<String, List<ConstantItem>> cloneConstMap() {

        Map<String, List<ConstantItem>> map = new HashMap<>();

        for (Map.Entry<String, List<ConstantItem>> entry : ConstantService.constMap.entrySet()) {

            List<ConstantItem> constantItems = new ArrayList<>();
            for (ConstantItem constantItem : entry.getValue()) {
                constantItems.add(new ConstantItem(constantItem.getCode(), constantItem.getName()));
            }

            map.put(entry.getKey(), constantItems);
        }

        return map;
    }


    private static Set<BeanDefinition> getConstantBeanSet() {

        Set<BeanDefinition> constBeanSet = new HashSet<>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources("classpath*:com/coderman/**/constant/*.class");

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
