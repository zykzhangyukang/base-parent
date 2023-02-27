package com.coderman.sync.init;

import com.coderman.sync.config.MultiDatasourceConfig;
import com.coderman.sync.db.*;
import com.coderman.sync.util.SyncBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Lazy(value = false)
@Component
public class DataSourceInitializer {

    @Resource
    private MultiDatasourceConfig config;

    /**
     * 初始化数据源
     */
    public void init(){


        List<AbstractDbConfig> dbConfigList = DbConfigBuilder.build(config);

        if(CollectionUtils.isNotEmpty(dbConfigList)){


            for (AbstractDbConfig dbConfig : dbConfigList) {


                if(dbConfig instanceof MySQLConfig){

                    SyncBeanUtil.registerMySQLDataSource((MySQLConfig) dbConfig);

                }else if( dbConfig instanceof MSSQLConfig){

                    SyncBeanUtil.registerMSSQLDataSource((MSSQLConfig) dbConfig);

                }else if(dbConfig instanceof OracleConfig){

                    SyncBeanUtil.registerOracleDataSource((OracleConfig) dbConfig);

                }else if(dbConfig instanceof MongoConfig){

                    SyncBeanUtil.registerMongoDataSource((MongoConfig) dbConfig);
                }

            }

        }

    }
}
