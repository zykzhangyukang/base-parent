package com.coderman.sync.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class JdbcConfig extends AbstractDbConfig{

    /**
     * 最小空闲连接数
     */
    private String minIdle;


    /**
     * 最大空闲连接数
     */
    private String maxIdle;


    /**
     * 最大连接数
     */
    private String maxActive;


    /**
     * 最大等待时间
     */
    private String maxWait;


    /**
     * 事务超时时间
     */
    private String transTimeout;

    private String timeBetweenEvictionRunsMillis;
    private String minEvictableIdleTimeMillis;
    private String validationQuery = "SELECT 'x'";
    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;



}
