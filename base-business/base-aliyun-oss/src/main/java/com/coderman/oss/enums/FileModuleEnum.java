package com.coderman.oss.enums;

/**
 * @author ：zhangyukang
 * @date ：2023/09/27 10:57
 */
public enum  FileModuleEnum {

    /**
     * 公共模块
     */
    COMMON_MODULE("common", "公共模块"),

    /**
     * 商品模块
     */
    PRODUCT_MODULE("product" , "商品模块"),

    /**
     * 商品模块
     */
    USER_MODULE("user" , "用户模块")
    ;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    FileModuleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
