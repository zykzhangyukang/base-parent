package com.coderman.sync.config;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sync.jdbc")
@Data
public class MultiDatasourceConfig {

    private String commonMaxIdle;
    private String commonMaxWait;
    private String commonMinIdle;
    private String commonMaxActive;
    private String commonTimeout;
    private String mysqlUsername;
    private String mysqlPassword;
    private String mysqlInitialSize;

    private String mysqlMinEvictableIdleTimeMillis;
    private String mysqlTimeBetweenEvictionRunsMillis;
    private String mysqlTestOnBorrow;
    private String mysqlTestOnReturn;
    private String mysqlTestWhileIdle;

    @Data
    public static final class SyncDbConfig {
        private String dbname;
        private String url;
        private String username;
        private String password;


        private String maxIdle;
        private String maxWait;
        private String minIdle;
        private String maxActive;
        private String timeout;


        private String minEvictableIdleTimeMillis;
        private String timeBetweenEvictionRunsMillis;
        private String testOnBorrow;
        private String testOnReturn;
        private String testWhileIdle;

        // mysql
        private String initialSize;

        // sqlserver
        private String logAbandoned;
        private String removeAbandoned;
        private String removeAbandonedTimeout;

        // mongo
        private String db;
        private Integer connectionsPerHost;
        private Integer connectTimeout;
        private Integer maxWaitTime;
        private Integer serverSelectionTimeout;
        private String socketKeepAlive;
        private Integer socketTimeout;
        private Integer threadsAllowedToBlockForConnectionMultiplier;

    }

    private List<SyncDbConfig> dbList;


    /**
     * 获取所有数据库名称
     *
     * @return
     */
    public List<String> listDatabases(){

        List<String> list = null;

        if(CollectionUtils.isNotEmpty(dbList)){

            list = new ArrayList<>(dbList.size());

            for (SyncDbConfig syncDbConfig : dbList) {

                list.add(syncDbConfig.getDbname());
            }
        }

        return list;
    }
}
