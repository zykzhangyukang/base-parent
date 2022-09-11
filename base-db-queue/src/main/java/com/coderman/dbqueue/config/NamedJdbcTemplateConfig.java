package com.coderman.dbqueue.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author coderman
 * @Title: 配置数据源
 * @Description: TOD
 * @date 2022/6/1814:41
 */
@Configuration
@SuppressWarnings("all")
public class NamedJdbcTemplateConfig {


    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate){

        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
