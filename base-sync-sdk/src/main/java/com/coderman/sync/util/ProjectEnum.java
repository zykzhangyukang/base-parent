package com.coderman.sync.util;

/**
 * @author coderman
 */

public enum ProjectEnum {

    /**
     * 权限系统
     */
    AUTH("auth"),

    /**
     * 课程系统
     */
    BIZEDU("bizedu"),

    /**
     * 权限系统-sync
     */
    AUTH_SYNC("auth_sync");


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
