package com.coderman.sync.util;

public enum ProjectEnum {

    /**
     * 数据库1
     */
    SYS1("sys1"),


    /**
     * 数据库2
     */
    SYS2("sys2");




    private String key;


    ProjectEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
