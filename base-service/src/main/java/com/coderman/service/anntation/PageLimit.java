package com.coderman.service.anntation;

import com.coderman.api.constant.CommonConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author coderman
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageLimit {

    /**
     * 分页最多限制显示多少条
     *
     * @return
     */
    String limitTotal() default CommonConstant.SYS_PAGE_LIMIT;
}
