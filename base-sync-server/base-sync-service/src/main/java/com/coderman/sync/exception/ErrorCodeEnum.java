package com.coderman.sync.exception;

public enum  ErrorCodeEnum {

    /**
     * 数据库配置不存在
     */
    DB_NOT_CONFIG(9),

    /**
     * 无法获取数据库连接
     */
    DB_NOT_CONNECT(10),


    /**
     * SQL参数多余
     */
    SQL_PARAM_EXCEED(20),

    /**
     * SQL参数数据不匹配
     */
    SQL_PARAM_NUM_NOT_MATCH(30);


    private int key;

    ErrorCodeEnum(int key){

        this.key = key;
    }
}
