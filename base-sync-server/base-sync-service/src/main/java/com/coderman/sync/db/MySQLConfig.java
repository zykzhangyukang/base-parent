package com.coderman.sync.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MySQLConfig extends JdbcConfig{


    /**
     * 初始化连接数
     */
    private String initialSize;

    private String poolPreparedStatements = "true";
    private String maxPoolPreparedStatementPerConnectionSize = "20";
    private String filters = "stat";



}
