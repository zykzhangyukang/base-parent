package com.coderman.auth.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.coderman.service.config.BasicTransactionConfig;
import org.aopalliance.aop.Advice;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

//@org.springframework.context.annotation.Configuration
//@EnableConfigurationProperties(MybatisProperties.class)
public class AuthDatasourceConfig extends BasicTransactionConfig {


    public static final String PACKAGE = "com.coderman.*.dao";

    public static final String MAPPER_LOCATION = "classpath:com/coderman/*/dao/*/*.xml";

    public static final String EXPRESSION = "execution(* com.coderman.auth..service..*(..))";


    @Bean(value = "datasource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.auth")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(value = "syncJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier(value = "datasource") DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    @Bean(value = "authTransactionManager")
    public DataSourceTransactionManager authTransactionManager(@Qualifier(value = "datasource") DataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory order1SqlSessionFactory(@Qualifier(value = "datasource") DataSource dataSource, MybatisProperties properties) throws Exception {

        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));

        Configuration configuration = properties.getConfiguration();
        if (configuration != null && !StringUtils.hasText(properties.getConfigLocation())) {

            configuration = new Configuration();

        }
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(value = "authTxAdvice")
    public TransactionInterceptor authTxAdvice(@Qualifier(value = "authTransactionManager") PlatformTransactionManager transactionManager) {
        return transactionInterceptor(transactionManager);
    }

    @Bean
    public Advisor authAdvisor(@Qualifier(value = "authTxAdvice") Advice advice) {
        return advisor(advice, EXPRESSION);
    }
}
