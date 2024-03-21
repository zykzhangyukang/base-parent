package com.coderman.service.anntation;

import java.lang.annotation.*;

/**
 * @author coderman
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogErrorParam {

    String[] value() default {};
}
