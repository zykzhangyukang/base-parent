package com.coderman.sync.util;

public enum ProjectEnum {

    /**
     * 客户系统
     */
    MEMBER("member"),

    /**
     * 商品系统
     */
    SKU("sku"),

    /**
     * 索引系统
     */
    PIM("pim"),

    /**
     * 下单系统
     */
    ORDER("order");


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
