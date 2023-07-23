package com.coderman.sync.util;

public enum ProjectEnum {

    /**
     * 权限系统
     */
    AUTH("auth"),

    /**
     * 测试系统
     */
    DEMO("demo"),

    /**
     * 索引系统
     */
    PIM("pim"),

    /**
     * 采购系统
     */
    PMS("pms"),

    /**
     * 仓储系统
     */
    WMS("wms"),

    /**
     * 营销后台
     */
    MMS("mms"),

    /**
     * 购物车
     */
    CART("cart"),

    /**
     * 营销前台
     */
    MARKET("market"),

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
