package com.coderman.sync.init;

import com.coderman.service.config.PropertyConfig;
import com.coderman.sync.db.*;
import com.coderman.sync.util.SyncBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Lazy(value = false)
@Component
public class DataSourceInitializer {


    /**
     * 初始化数据源
     */
    public void init(){


        Map<String,String> paramMap = PropertyConfig.getDictMap("jdbc");

        List<AbstractDbConfig> dbConfigList = DbConfigBuilder.build(paramMap);

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
