package com.coderman.sync.util;

/**
 * @author coderman
 */

public enum ProjectEnum {

    /**
     * 后台系统
     */
    ADMIN("admin"),

    /**
     * 后台sync系统
     */
    SYNC("sync"),

    /**
     * 后台日志系统
     */
    LOG("log");


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
