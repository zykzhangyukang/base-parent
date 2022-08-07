package com.coderman.api.anntation;

import java.lang.annotation.*;

/**
 * @author coderman
 * @Title: 常量组注解
 * @Description: TOD
 * @date 2022/1/2519:47
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConstantList {

    /**
     * 常量组
     *
     * @return
     */
    String group();


    /**
     * 常量名
     *
     * @return
     */
    String name();
}
