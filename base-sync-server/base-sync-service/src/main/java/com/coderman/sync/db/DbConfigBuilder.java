package com.coderman.sync.db;

import com.coderman.sync.config.MultiDatasourceConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DbConfigBuilder {

    private static MultiDatasourceConfig config;


    public static List<AbstractDbConfig> build(MultiDatasourceConfig multiDatasourceConfig) {

        config = multiDatasourceConfig;

        List<MultiDatasourceConfig.SyncDbConfig> dbList = multiDatasourceConfig.getDbList();

        if(CollectionUtils.isEmpty(dbList)){

            return Collections.emptyList();
        }

        List<AbstractDbConfig> configList = new ArrayList<>(dbList.size());


        for (MultiDatasourceConfig.SyncDbConfig datasource : dbList) {

            String url = datasource.getUrl();

            if (StringUtils.startsWith(url, "jdbc:mysql")) {

                configList.add(getMySQLConfig(datasource));

            } else if (StringUtils.startsWith(url, "jdbc:jtds:sqlserver:")) {

                configList.add(getMSSQLDbConfig(datasource,MSSQLConfig.DRIVER_JTDS));

            } else if (StringUtils.startsWith(url, "jdbc:sqlserver:")) {


                configList.add(getMSSQLDbConfig(datasource,MSSQLConfig.DRIVER_MICROSOFT));


            } else if (StringUtils.startsWith(url, "oracle")) {

                configList.add(getOracleDbConfig(datasource));

            } else if (StringUtils.equalsIgnoreCase(url, ",mongo")) {

                configList.add(getMongoConfig(datasource));

            }

        }

        return configList;
    }

    private static MongoConfig getMongoConfig(MultiDatasourceConfig.SyncDbConfig syncDbConfig ) {

        // mongo 配置
        String key = syncDbConfig.getDbname();
        String url = syncDbConfig.getUrl();
        String username = syncDbConfig.getUsername();
        String password = syncDbConfig.getPassword();
        String db = syncDbConfig.getDb();
        Integer connectionsPerHost = syncDbConfig.getConnectionsPerHost();
        Integer maxWaitTime = syncDbConfig.getMaxWaitTime();
        Integer connectTimeout = syncDbConfig.getConnectTimeout();
        Integer serverSelectionTimeout = syncDbConfig.getServerSelectionTimeout();
        String socketKeepAlive = syncDbConfig.getSocketKeepAlive();
        Integer socketTimeout = syncDbConfig.getSocketTimeout();
        Integer threadsAllowedToBlockForConnectionMultiplier = syncDbConfig.getThreadsAllowedToBlockForConnectionMultiplier();


        // 跳过sync的配置
        if(StringUtils.equalsIgnoreCase("sync",key)){
            return null;
        }

        MongoConfig mongoConfig = new MongoConfig();
        mongoConfig.setBeanId(key);
        mongoConfig.setUrl(url);
        mongoConfig.setUserName(username);
        mongoConfig.setPassword(password);
        mongoConfig.setDb(db);
        mongoConfig.setConnectionsPerHost(connectionsPerHost);
        mongoConfig.setConnectTimeout(connectTimeout);
        mongoConfig.setMaxWaitTime(maxWaitTime);
        mongoConfig.setServerSelectionTimeout(serverSelectionTimeout);
        mongoConfig.setSocketKeepAlive(socketKeepAlive);
        mongoConfig.setSocketTimeout(socketTimeout);
        mongoConfig.setThreadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);

        return mongoConfig;
    }


    private static AbstractDbConfig getOracleDbConfig(MultiDatasourceConfig.SyncDbConfig syncDbConfig) {

        OracleConfig oracleConfig = new OracleConfig();

        oracleConfig.setBeanId(syncDbConfig.getDbname());
        oracleConfig.setDriverClassName(OracleConfig.DRIVER_ORACLE);


        // 读取配置
        String maxIdle = Optional.ofNullable(syncDbConfig.getMaxIdle()).orElse(config.getCommonMaxIdle());
        String maxActive = Optional.ofNullable(syncDbConfig.getMaxActive()).orElse(config.getCommonMaxActive());
        String minIdle = Optional.ofNullable(syncDbConfig.getMinIdle()).orElse(config.getCommonMinIdle());
        String maxWait = Optional.ofNullable(syncDbConfig.getMaxWait()).orElse(config.getCommonMaxWait());
        String username = Optional.ofNullable(syncDbConfig.getUsername()).orElse(config.getMysqlUsername());
        String timeout = Optional.ofNullable(syncDbConfig.getTimeout()).orElse(config.getCommonTimeout());
        String password = Optional.ofNullable(syncDbConfig.getPassword()).orElse(config.getMysqlPassword());


        oracleConfig.setMaxIdle(maxIdle);
        oracleConfig.setMaxActive(maxActive);
        oracleConfig.setMaxWait(maxWait);
        oracleConfig.setMinIdle(minIdle);
        oracleConfig.setTransTimeout(timeout);

        oracleConfig.setUrl(syncDbConfig.getUrl());
        oracleConfig.setUserName(username);
        oracleConfig.setPassword(password);

        String timeBetweenEvictionRunsMillis = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());
        String minEvictableIdleTimeMillis = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());
        String testOnBorrow = Optional.ofNullable(syncDbConfig.getTestOnBorrow()).orElse(config.getMysqlTestOnBorrow());
        String testOnReturn = Optional.ofNullable(syncDbConfig.getTestOnReturn()).orElse(config.getMysqlTestOnReturn());
        String testWhileIdle = Optional.ofNullable(syncDbConfig.getTestWhileIdle()).orElse(config.getMysqlTestWhileIdle());


        oracleConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        oracleConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        oracleConfig.setTestOnBorrow(testOnBorrow);
        oracleConfig.setTestOnReturn(testOnReturn);
        oracleConfig.setTestWhileIdle(testWhileIdle);

        return oracleConfig;
    }

    private static AbstractDbConfig getMSSQLDbConfig(MultiDatasourceConfig.SyncDbConfig syncDbConfig,  String driverClassName) {

        MSSQLConfig mssqlConfig = new MSSQLConfig();

        mssqlConfig.setDriverClassName(driverClassName);
        mssqlConfig.setBeanId(syncDbConfig.getDbname());

        // 读取配置
        String maxActive = Optional.ofNullable(syncDbConfig.getMaxActive()).orElse(config.getCommonMaxActive());
        String maxIdle = Optional.ofNullable(syncDbConfig.getMaxIdle()).orElse(config.getCommonMaxIdle());
        String maxWait = Optional.ofNullable(syncDbConfig.getMaxWait()).orElse(config.getCommonMaxWait());
        String username = Optional.ofNullable(syncDbConfig.getUsername()).orElse(config.getMysqlUsername());
        String minIdle = Optional.ofNullable(syncDbConfig.getMinIdle()).orElse(config.getCommonMinIdle());
        String timeout = Optional.ofNullable(syncDbConfig.getTimeout()).orElse(config.getCommonTimeout());
        String password = Optional.ofNullable(syncDbConfig.getPassword()).orElse(config.getMysqlPassword());

        String timeBetweenEvictionRunsMillis = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());
        String minEvictableIdleTimeMillis = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());
        String testOnReturn = Optional.ofNullable(syncDbConfig.getTestOnReturn()).orElse(config.getMysqlTestOnReturn());
        String testOnBorrow = Optional.ofNullable(syncDbConfig.getTestOnBorrow()).orElse(config.getMysqlTestOnBorrow());
        String testWhileIdle = Optional.ofNullable(syncDbConfig.getTestWhileIdle()).orElse(config.getMysqlTestWhileIdle());


        mssqlConfig.setMaxActive(maxActive);
        mssqlConfig.setMaxIdle(maxIdle);
        mssqlConfig.setMinIdle(minIdle);
        mssqlConfig.setMaxWait(maxWait);
        mssqlConfig.setTransTimeout(timeout);

        mssqlConfig.setUrl(syncDbConfig.getUrl());
        mssqlConfig.setUserName(username);
        mssqlConfig.setPassword(password);

        mssqlConfig.setLogAbandoned(syncDbConfig.getLogAbandoned());
        mssqlConfig.setRemoveAbandoned(syncDbConfig.getRemoveAbandoned());
        mssqlConfig.setRemoveAbandonedTimeout(syncDbConfig.getRemoveAbandonedTimeout());

        mssqlConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        mssqlConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        mssqlConfig.setTestOnBorrow(testOnBorrow);
        mssqlConfig.setTestOnReturn(testOnReturn);
        mssqlConfig.setTestWhileIdle(testWhileIdle);

        return mssqlConfig;

    }

    private static AbstractDbConfig getMySQLConfig(MultiDatasourceConfig.SyncDbConfig syncDbConfig) {

        MySQLConfig mySQLConfig = new MySQLConfig();

        String key = syncDbConfig.getDbname();

        // 注册bean key
        mySQLConfig.setBeanId(key);

        // 读取配置
        String maxActive = Optional.ofNullable(syncDbConfig.getMaxActive()).orElse(config.getCommonMaxActive());
        String maxIdle = Optional.ofNullable(syncDbConfig.getMaxIdle()).orElse(config.getCommonMaxIdle());
        String maxWait = Optional.ofNullable(syncDbConfig.getMaxWait()).orElse(config.getCommonMaxWait());
        String minIdle = Optional.ofNullable(syncDbConfig.getMinIdle()).orElse(config.getCommonMinIdle());
        String timeout = Optional.ofNullable(syncDbConfig.getTimeout()).orElse(config.getCommonTimeout());
        String username = Optional.ofNullable(syncDbConfig.getUsername()).orElse(config.getMysqlUsername());
        String password = Optional.ofNullable(syncDbConfig.getPassword()).orElse(config.getMysqlPassword());
        String initialSize = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());

        String timeBetweenEvictionRunsMillis = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());

        String minEvictableIdleTimeMillis = Optional.ofNullable(syncDbConfig.getInitialSize()).orElse(config.getMysqlInitialSize());
        String testOnBorrow = Optional.ofNullable(syncDbConfig.getTestOnBorrow()).orElse(config.getMysqlTestOnBorrow());
        String testWhileIdle = Optional.ofNullable(syncDbConfig.getTestWhileIdle()).orElse(config.getMysqlTestWhileIdle());
        String testOnReturn = Optional.ofNullable(syncDbConfig.getTestOnReturn()).orElse(config.getMysqlTestOnReturn());


        mySQLConfig.setMaxActive(maxActive);
        mySQLConfig.setMaxIdle(maxIdle);
        mySQLConfig.setMaxWait(maxWait);
        mySQLConfig.setMinIdle(minIdle);
        mySQLConfig.setTransTimeout(timeout);

        mySQLConfig.setUrl(syncDbConfig.getUrl());
        mySQLConfig.setUserName(username);
        mySQLConfig.setPassword(password);

        mySQLConfig.setInitialSize(initialSize);
        mySQLConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        mySQLConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        mySQLConfig.setTestOnBorrow(testOnBorrow);
        mySQLConfig.setTestOnReturn(testOnReturn);
        mySQLConfig.setTestWhileIdle(testWhileIdle);

        return mySQLConfig;
    }
}
