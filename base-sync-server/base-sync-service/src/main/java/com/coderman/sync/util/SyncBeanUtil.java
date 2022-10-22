package com.coderman.sync.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.db.JdbcConfig;
import com.coderman.sync.db.MySQLConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;


public class SyncBeanUtil {

    private final static Logger logger = LoggerFactory.getLogger(SyncBeanUtil.class);


    /**
     * 注册MySQL的数据源
     */
    public static void registerMySQLDataSource(MySQLConfig config){

        ConfigurableApplicationContext context = (ConfigurableApplicationContext) SpringContextUtil.getApplicationContext();

        DefaultListableBeanFactory beanFactory =(DefaultListableBeanFactory) context.getBeanFactory();

        BeanDefinitionBuilder definitionBuilder =SyncBeanUtil.createDataSourceBeanDefinitionBuilder(config,false);

        definitionBuilder.setInitMethodName("init");

        definitionBuilder.addPropertyValue("initialSize",config.getInitialSize());
        definitionBuilder.addPropertyValue("poolPreparedStatements",config.getPoolPreparedStatements());
        definitionBuilder.addPropertyValue("maxPoolPreparedStatementPerConnectionSize","20");
        definitionBuilder.addPropertyValue("filters","stat");


        // 注册数据源
        beanFactory.registerBeanDefinition(config.getBeanId(),definitionBuilder.getBeanDefinition());

        // 注册模板
        SyncBeanUtil.registerTemplate(config.getBeanId(),config.getTransTimeout(),beanFactory);

        // 注册数据库名称与类型的关系
        SyncContext.getContext().relateDbType(config.getBeanId(), SyncConstant.DB_TYPE_MYSQL);

    }

    private static void registerTemplate(String dataSourceId, String transTimeout, DefaultListableBeanFactory beanFactory) {

        // 先注册jdbcTemplate
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class);

        beanDefinitionBuilder.addPropertyReference("dataSource",dataSourceId);

        // 查询超时时间,设置为5s
        beanDefinitionBuilder.addPropertyValue("queryTimeout",5);
        beanFactory.registerBeanDefinition(dataSourceId+"_template",beanDefinitionBuilder.getBeanDefinition());

        // 注册TransactionManager
        BeanDefinitionBuilder tmBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        tmBuilder.addPropertyReference("dataSource",dataSourceId);
        beanFactory.registerBeanDefinition(dataSourceId+"_tm",tmBuilder.getBeanDefinition());

        // 注册TransactionTemplate
        BeanDefinitionBuilder transBuilder =BeanDefinitionBuilder.genericBeanDefinition(TransactionTemplate.class);
        transBuilder.addPropertyReference("transactionManager",dataSourceId+"_tm");
        transBuilder.addPropertyValue("isolationLevelName","ISOLATION_DEFAULT");
        transBuilder.addPropertyValue("propagationBehaviorName","PROPAGATION_REQUIRED");
        transBuilder.addPropertyValue("timeout",transTimeout);

        beanFactory.registerBeanDefinition(dataSourceId+"_trans",transBuilder.getBeanDefinition());
    }


    @SuppressWarnings("all")
    private static BeanDefinitionBuilder createDataSourceBeanDefinitionBuilder(JdbcConfig config,boolean isMSSQL){

        BeanDefinitionBuilder definitionBuilder =BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource.class);

        if(isMSSQL){

            // definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BasicDataSource.class)
        }

        definitionBuilder.setDestroyMethodName("close");
        definitionBuilder.setLazyInit(false);
        definitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_NO);
        definitionBuilder.addPropertyValue("url",config.getUrl());
        definitionBuilder.addPropertyValue("username",config.getUserName());
        definitionBuilder.addPropertyValue("password",config.getPassword());
        definitionBuilder.addPropertyValue("validationQuery",config.getValidationQuery());

        if(!config.getUrl().contains("jdbc:oracle")){


            definitionBuilder.addPropertyValue("minIdle",config.getMinIdle());
            definitionBuilder.addPropertyValue("maxIdle",config.getMaxIdle());
            definitionBuilder.addPropertyValue("maxActive",config.getMaxActive());
            definitionBuilder.addPropertyValue("maxWait",config.getMaxWait());


            definitionBuilder.addPropertyValue("timeBetweenEvictionRunsMillis",config.getTimeBetweenEvictionRunsMillis());
            definitionBuilder.addPropertyValue("minEvictableIdleTimeMillis",config.getMinEvictableIdleTimeMillis());
            definitionBuilder.addPropertyValue("testWhileIdle",config.getTestWhileIdle());
            definitionBuilder.addPropertyValue("testOnBorrow",config.getTestOnBorrow());
            definitionBuilder.addPropertyValue("testOnReturn",config.getTestOnReturn());

        }


        return definitionBuilder;
    }
}
