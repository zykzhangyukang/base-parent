package com.coderman.service.config;

import com.coderman.service.publisher.ConfigChangePublisher;
import com.coderman.service.util.SpringContextUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PropertyConfig {

    @Getter
    private static Map<String,String> configMap = new HashMap<>();


    @Getter
    private static Map<String,Map<String,String>> dictMap =  new HashMap<>();

    /**
     * 加入配置
     * @param key 配置key
     * @param value 配置value
     */
    public static void put(String key,String value){

        if(configMap.containsKey(key) && !configMap.get(key).equals(value)){

            ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();

            if(null!=applicationContext){
                ConfigChangePublisher publisher = SpringContextUtil.getBean(ConfigChangePublisher.class);
                publisher.publish(key,configMap.get(key),value);
            }
        }

        configMap.put(key,value);
    }


    protected static void putDict(String namespace,Map<String,String> valueMap){

        dictMap.put(namespace,valueMap);
    }

    protected static void removeDict(String namespace,String key){

        if(dictMap.containsKey(namespace)){

            dictMap.get(namespace).remove(key);
        }


    }


    protected static void addDictItem(String namespace,String key,String value){

        if(dictMap.containsKey(namespace)){

            dictMap.get(namespace).put(key,value.trim());
        }

    }

    public static String getConfigValue(String key){

        String value = configMap.get(key);
        if(value == null){

            value = StringUtils.EMPTY;
        }

        return value;
    }


    public static Map<String,String> getDictMap(String namespace){

        if(MapUtils.isEmpty(dictMap) || MapUtils.isEmpty(dictMap.get(namespace.trim()))){


            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(dictMap.get(namespace.trim()));

    }


    public static Map<String,Map<String,String>> getAllDictMap(){

        if(MapUtils.isEmpty(dictMap)){


            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(dictMap);

    }


    protected static void remove(String key){

        configMap.remove(key);
    }

    public static void setConfigMap(Map<String, String> configMap) {
        PropertyConfig.configMap = configMap;
    }

    public static void setDictMap(Map<String, Map<String, String>> dictMap) {
        PropertyConfig.dictMap = dictMap;
    }
}
