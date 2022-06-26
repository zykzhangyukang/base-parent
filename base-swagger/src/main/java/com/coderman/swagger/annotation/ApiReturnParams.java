package com.coderman.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author coderman
 * @Title: 返回结果字段
 * @Description: TOD
 * @date 2022/3/1512:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiReturnParams {


    ApiReturnParam[] value();
}
