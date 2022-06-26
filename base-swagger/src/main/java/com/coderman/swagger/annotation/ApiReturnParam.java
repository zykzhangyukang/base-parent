package com.coderman.swagger.annotation;

import java.lang.annotation.*;

/**
 * @author coderman
 * @Title: swagger返回结果
 * @Description: TOD
 * @date 2022/3/1512:25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiReturnParam {

    /**
     * 包含的属性
     *
     * @return
     */
    String[] value();


    /**
     * VO名称
     * @return
     */
    String name();
}
