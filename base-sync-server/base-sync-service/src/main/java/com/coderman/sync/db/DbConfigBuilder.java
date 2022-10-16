package com.coderman.sync.db;

import com.coderman.service.util.DesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbConfigBuilder {


    public static List<AbstractDbConfig> build(Map<String,String> configMap){

        List<AbstractDbConfig> configList = new ArrayList<>();


        for (Map.Entry<String, String> config : configMap.entrySet()) {

            if(config.getKey().endsWith("$url")){


                String key = config.getKey().substring(0,config.getKey().indexOf("$"));

                if(config.getValue().startsWith("jdbc:mysql")){

                    configList.add(getMySQLConfig(configMap,config,key));
                }

            }

        }

        return configList;
    }

    private static AbstractDbConfig getMySQLConfig(Map<String, String> configMap, Map.Entry<String, String> config, String key) {

        MySQLConfig mySQLConfig = new MySQLConfig();

        mySQLConfig.setBeanId(key);
        mySQLConfig.setMaxActive(configMap.containsKey(key+"$maxActive") ? configMap.get(key+"$maxActive") : configMap.get("common$maxActive"));
        mySQLConfig.setMaxIdle(configMap.containsKey(key+"$maxIdle") ? configMap.get(key+"$maxIdle") : configMap.get("common$maxIdle"));
        mySQLConfig.setMaxWait(configMap.containsKey(key+"$maxWait") ? configMap.get(key+"$maxWait") : configMap.get("common$maxWait"));
        mySQLConfig.setMinIdle(configMap.containsKey(key+"$minIdle") ? configMap.get(key+"$minIdle") : configMap.get("common$minIdle"));
        mySQLConfig.setTransTimeout(configMap.containsKey(key+"$timeout") ? configMap.get(key+"$timeout") : configMap.get("common$timeout"));

        mySQLConfig.setUrl(config.getValue());
        mySQLConfig.setUserName(configMap.containsKey(key+"$username") ? configMap.get(key+"$username") : configMap.get("mysql$username"));
        mySQLConfig.setPassword(DesUtil.decrypt(configMap.containsKey(key+"$password")  ? configMap.get(key+"$password") : configMap.get("mysql$password")));

        mySQLConfig.setInitialSize(configMap.containsKey(key+"$initialSize") ? configMap.get(key+"$initialSize") : configMap.get("mysql$initialSize"));
        mySQLConfig.setMinEvictableIdleTimeMillis(configMap.containsKey(key+"$minEvictableIdleTimeMillis") ? configMap.get(key+"$minEvictableIdleTimeMillis") : configMap.get("mysql$minEvictableIdleTimeMillis"));
        mySQLConfig.setTimeBetweenEvictionRunsMillis(configMap.containsKey(key+".timeBetweenEvictionRunsMillis") ? configMap.get(key+"$timeBetweenEvictionRunsMillis") : configMap.get("mysql$timeBetweenEvictionRunsMillis"));
        mySQLConfig.setTestOnBorrow(configMap.containsKey(key+"$testOnBorrow") ? configMap.get(key+"$testOnBorrow") : configMap.get("mysql$testOnBorrow"));
        mySQLConfig.setTestOnReturn(configMap.containsKey(key+"$testOnReturn") ? configMap.get(key+"$testOnReturn") : configMap.get("mysql$testOnReturn"));
        mySQLConfig.setTestWhileIdle(configMap.containsKey(key+"$testWhileIdle") ? configMap.get(key+"$testWhileIdle") : configMap.get("mysql$testWhileIdle"));

        return mySQLConfig;
    }
}
