package com.coderman.sync.db;

import com.coderman.service.util.DesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * jdbc:
 *   common$maxIdle: 20
 *   common$maxWait: 1000
 *   common$minIdle: 10
 *   common$maxActive: 800
 *   common$timeout: 20
 *   mysql$username: root
 *   mysql$password: 123456
 *   mysql$initialSize: 10
 *   mysql$minEvictableIdleTimeMillis: 40000
 *   mysql$timeBetweenEvictionRunsMillis: 10000
 *   mysql$testOnBorrow: false
 *   mysql$testOnReturn: false
 *   mysql$testWhileIdle: true
 *   # 业务系统1
 *   datasource1$url: jdbc:mysql://localhost:3306/datasource1?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8
 *   datasource1$username: root
 *   datasource1$password: 2120FA78BC6EB99B019C119BD8067DB0
 *   # 业务系统2
 *   datasource2$url: jdbc:mysql://localhost:3306/datasource2?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8
 *   datasource2$username: root
 *   datasource2$password: 2120FA78BC6EB99B019C119BD8067DB0
 */
@Deprecated
public class DbConfigBuilderBak {


    public static List<AbstractDbConfig> build(Map<String, String> configMap) {

        List<AbstractDbConfig> configList = new ArrayList<>();


        for (Map.Entry<String, String> config : configMap.entrySet()) {

            if (config.getKey().endsWith("$url")) {


                String key = config.getKey().substring(0, config.getKey().indexOf("$"));

                if (config.getValue().startsWith("jdbc:mysql")) {

                    configList.add(getMySQLConfig(configMap, config, key));
                } else if (config.getValue().startsWith("jdbc:jtds:sqlserver:")) {

                    configList.add(getMSSQLDbConfig(key, config.getValue(), configMap, MSSQLConfig.DRIVER_JTDS));
                } else if (config.getValue().startsWith("jdbc:sqlserver:")) {

                    configList.add(getMSSQLDbConfig(key, config.getValue(), configMap, MSSQLConfig.DRIVER_MICROSOFT));
                } else if (config.getValue().startsWith("jdbc:oracle")) {

                    configList.add(getOracleDbConfig(key, config.getValue(), configMap, OracleConfig.DRIVER_ORACLE));
                }

            } else if (config.getKey().endsWith(".mongo.replica")) {

                MongoConfig mongoConfig = getMongoConfig(configMap, config);
                if (mongoConfig != null) {

                    configList.add(mongoConfig);
                }

            }

        }

        return configList;
    }

    private static MongoConfig getMongoConfig(Map<String, String> configMap, Map.Entry<String, String> config) {

        // mongo 配置
        String key = config.getKey().substring(0, config.getKey().indexOf("."));

        // 跳过sync的配置
        if ("sync".equalsIgnoreCase(key)) {
            return null;
        }

        MongoConfig mongoConfig = new MongoConfig();
        mongoConfig.setBeanId(key);
        mongoConfig.setUrl(config.getValue());
        mongoConfig.setUserName(configMap.containsKey(key + ".mongo.username") ? configMap.get(key + ".mongo.username") : configMap.get("mongo.username"));
        mongoConfig.setPassword(DesUtil.decrypt(configMap.containsKey(key + ".mongo.password") ? configMap.get(key + ".mongo.password") : configMap.get("mongo.password")));
        mongoConfig.setDb(configMap.get(key + ".mongo.db"));
        mongoConfig.setConnectionsPerHost(Integer.parseInt(configMap.containsKey(key + ".mongo.connectionsPerHost") ? configMap.get(key + ".mongo.connectionsPerHost") : configMap.get("mongo.connectionsPerHost")));
        mongoConfig.setConnectTimeout(Integer.parseInt(configMap.containsKey(key + ".mongo.connectTimeout") ? configMap.get(key + ".mongo.connectTimeout") : configMap.get("mongo.connectTimeout")));
        mongoConfig.setMaxWaitTime(Integer.parseInt(configMap.containsKey(key + ".mongo.maxWaitTime") ? configMap.get(key + ".mongo.maxWaitTime") : configMap.get("mongo.maxWaitTime")));
        mongoConfig.setServerSelectionTimeout(Integer.parseInt(configMap.containsKey(key + ".mongo.serverSelectionTimeout") ? configMap.get(key + ".mongo.serverSelectionTimeout") : configMap.get("mongo.serverSelectionTimeout")));
        mongoConfig.setSocketKeepAlive(configMap.containsKey(key + ".mongo.socketKeepAlive") ? configMap.get(key + ".mongo.socketKeepAlive") : configMap.get("mongo.socketKeepAlive"));
        mongoConfig.setSocketTimeout(Integer.parseInt(configMap.containsKey(key + ".mongo.socketTimeout") ? configMap.get(key + ".mongo.socketTimeout") : configMap.get("mongo.socketTimeout")));
        mongoConfig.setThreadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(configMap.containsKey(key + ".mongo.threadsAllowedToBlockForConnectionMultiplier") ? configMap.get(key + ".mongo.threadsAllowedToBlockForConnectionMultiplier") : configMap.get("mongo.threadsAllowedToBlockForConnectionMultiplier")));

        return mongoConfig;
    }


    @SuppressWarnings("all")
    private static AbstractDbConfig getOracleDbConfig(String key, String configValue, Map<String, String> configMap, String driverOracle) {

        OracleConfig oracleConfig = new OracleConfig();

        oracleConfig.setBeanId(key);
        oracleConfig.setDriverClassName(driverOracle);

        oracleConfig.setMaxActive(configMap.containsKey(key + "$maxActive") ? configMap.get(key + "$maxActive") : configMap.get("common$maxActive"));
        oracleConfig.setMaxIdle(configMap.containsKey(key + "$maxIdle") ? configMap.get(key + "$maxIdle") : configMap.get("common$maxIdle"));
        oracleConfig.setMaxWait(configMap.containsKey(key + "$maxWait") ? configMap.get(key + "$maxWait") : configMap.get("common$maxWait"));
        oracleConfig.setMinIdle(configMap.containsKey(key + "$minIdle") ? configMap.get(key + "$minIdle") : configMap.get("common$minIdle"));
        oracleConfig.setTransTimeout(configMap.containsKey(key + "$timeout") ? configMap.get(key + "$timeout") : configMap.get("common$timeout"));

        oracleConfig.setUrl(configValue);
        oracleConfig.setUserName(configMap.containsKey(key + "$username") ? configMap.get(key + "$username") : configMap.get("mysql$username"));
        oracleConfig.setPassword(DesUtil.decrypt(configMap.containsKey(key + "$password") ? configMap.get(key + "$password") : configMap.get("mysql$password")));


        oracleConfig.setMinEvictableIdleTimeMillis(configMap.containsKey(key + "$minEvictableIdleTimeMillis") ? configMap.get(key + "$minEvictableIdleTimeMillis") : configMap.get("mysql$minEvictableIdleTimeMillis"));
        oracleConfig.setTimeBetweenEvictionRunsMillis(configMap.containsKey(key + ".timeBetweenEvictionRunsMillis") ? configMap.get(key + "$timeBetweenEvictionRunsMillis") : configMap.get("mysql$timeBetweenEvictionRunsMillis"));
        oracleConfig.setTestOnBorrow(configMap.containsKey(key + "$testOnBorrow") ? configMap.get(key + "$testOnBorrow") : configMap.get("mysql$testOnBorrow"));
        oracleConfig.setTestOnReturn(configMap.containsKey(key + "$testOnReturn") ? configMap.get(key + "$testOnReturn") : configMap.get("mysql$testOnReturn"));
        oracleConfig.setTestWhileIdle(configMap.containsKey(key + "$testWhileIdle") ? configMap.get(key + "$testWhileIdle") : configMap.get("mysql$testWhileIdle"));

        return oracleConfig;
    }

    @SuppressWarnings("all")
    private static AbstractDbConfig getMSSQLDbConfig(String key, String configValue, Map<String, String> configMap, String driverClassName) {

        MSSQLConfig mssqlConfig = new MSSQLConfig();

        mssqlConfig.setBeanId(key);
        mssqlConfig.setDriverClassName(driverClassName);

        mssqlConfig.setMaxActive(configMap.containsKey(key + "$maxActive") ? configMap.get(key + "$maxActive") : configMap.get("common$maxActive"));
        mssqlConfig.setMaxIdle(configMap.containsKey(key + "$maxIdle") ? configMap.get(key + "$maxIdle") : configMap.get("common$maxIdle"));
        mssqlConfig.setMaxWait(configMap.containsKey(key + "$maxWait") ? configMap.get(key + "$maxWait") : configMap.get("common$maxWait"));
        mssqlConfig.setMinIdle(configMap.containsKey(key + "$minIdle") ? configMap.get(key + "$minIdle") : configMap.get("common$minIdle"));
        mssqlConfig.setTransTimeout(configMap.containsKey(key + "$timeout") ? configMap.get(key + "$timeout") : configMap.get("common$timeout"));

        mssqlConfig.setUrl(configValue);
        mssqlConfig.setUserName(configMap.containsKey(key + "$username") ? configMap.get(key + "$username") : configMap.get("mysql$username"));
        mssqlConfig.setPassword(DesUtil.decrypt(configMap.containsKey(key + "$password") ? configMap.get(key + "$password") : configMap.get("mysql$password")));

        mssqlConfig.setLogAbandoned(configMap.containsKey(key + "logAbandoned") ? configMap.get(key + "logAbandoned") : configMap.get("logAbandoned"));
        mssqlConfig.setRemoveAbandoned(configMap.containsKey(key + "removeAbandoned") ? configMap.get(key + "removeAbandoned") : configMap.get("removeAbandoned"));
        mssqlConfig.setRemoveAbandonedTimeout(configMap.containsKey(key + "removeAbandonedTimeout") ? configMap.get(key + "removeAbandonedTimeout") : configMap.get("removeAbandonedTimeout"));

        mssqlConfig.setMinEvictableIdleTimeMillis(configMap.containsKey(key + "$minEvictableIdleTimeMillis") ? configMap.get(key + "$minEvictableIdleTimeMillis") : configMap.get("mysql$minEvictableIdleTimeMillis"));
        mssqlConfig.setTimeBetweenEvictionRunsMillis(configMap.containsKey(key + ".timeBetweenEvictionRunsMillis") ? configMap.get(key + "$timeBetweenEvictionRunsMillis") : configMap.get("mysql$timeBetweenEvictionRunsMillis"));
        mssqlConfig.setTestOnBorrow(configMap.containsKey(key + "$testOnBorrow") ? configMap.get(key + "$testOnBorrow") : configMap.get("mysql$testOnBorrow"));
        mssqlConfig.setTestOnReturn(configMap.containsKey(key + "$testOnReturn") ? configMap.get(key + "$testOnReturn") : configMap.get("mysql$testOnReturn"));
        mssqlConfig.setTestWhileIdle(configMap.containsKey(key + "$testWhileIdle") ? configMap.get(key + "$testWhileIdle") : configMap.get("mysql$testWhileIdle"));

        return mssqlConfig;

    }

    @SuppressWarnings("all")
    private static AbstractDbConfig getMySQLConfig(Map<String, String> configMap, Map.Entry<String, String> config, String key) {

        MySQLConfig mySQLConfig = new MySQLConfig();

        mySQLConfig.setBeanId(key);
        mySQLConfig.setMaxActive(configMap.containsKey(key + "$maxActive") ? configMap.get(key + "$maxActive") : configMap.get("common$maxActive"));
        mySQLConfig.setMaxIdle(configMap.containsKey(key + "$maxIdle") ? configMap.get(key + "$maxIdle") : configMap.get("common$maxIdle"));
        mySQLConfig.setMaxWait(configMap.containsKey(key + "$maxWait") ? configMap.get(key + "$maxWait") : configMap.get("common$maxWait"));
        mySQLConfig.setMinIdle(configMap.containsKey(key + "$minIdle") ? configMap.get(key + "$minIdle") : configMap.get("common$minIdle"));
        mySQLConfig.setTransTimeout(configMap.containsKey(key + "$timeout") ? configMap.get(key + "$timeout") : configMap.get("common$timeout"));

        mySQLConfig.setUrl(config.getValue());
        mySQLConfig.setUserName(configMap.containsKey(key + "$username") ? configMap.get(key + "$username") : configMap.get("mysql$username"));
        mySQLConfig.setPassword(DesUtil.decrypt(configMap.containsKey(key + "$password") ? configMap.get(key + "$password") : configMap.get("mysql$password")));

        mySQLConfig.setInitialSize(configMap.containsKey(key + "$initialSize") ? configMap.get(key + "$initialSize") : configMap.get("mysql$initialSize"));
        mySQLConfig.setMinEvictableIdleTimeMillis(configMap.containsKey(key + "$minEvictableIdleTimeMillis") ? configMap.get(key + "$minEvictableIdleTimeMillis") : configMap.get("mysql$minEvictableIdleTimeMillis"));
        mySQLConfig.setTimeBetweenEvictionRunsMillis(configMap.containsKey(key + ".timeBetweenEvictionRunsMillis") ? configMap.get(key + "$timeBetweenEvictionRunsMillis") : configMap.get("mysql$timeBetweenEvictionRunsMillis"));
        mySQLConfig.setTestOnBorrow(configMap.containsKey(key + "$testOnBorrow") ? configMap.get(key + "$testOnBorrow") : configMap.get("mysql$testOnBorrow"));
        mySQLConfig.setTestOnReturn(configMap.containsKey(key + "$testOnReturn") ? configMap.get(key + "$testOnReturn") : configMap.get("mysql$testOnReturn"));
        mySQLConfig.setTestWhileIdle(configMap.containsKey(key + "$testWhileIdle") ? configMap.get(key + "$testWhileIdle") : configMap.get("mysql$testWhileIdle"));

        return mySQLConfig;
    }
}
