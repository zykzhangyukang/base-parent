package com.coderman.service.dict;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @author coderman
 * @Title: 常量注解
 * @Description: TOD
 * @date 2022/1/2519:47
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Constant {

    /**
     * 是否允许冲突
     *
     * @return
     */
    boolean allowedConflict() default true;
}
