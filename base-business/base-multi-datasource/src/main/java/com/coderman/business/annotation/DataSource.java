package com.coderman.business.annotation;

import java.lang.annotation.*;

/**
 * @author coderman
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    String name() default "";
}
