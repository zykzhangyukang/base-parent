package com.coderman.service.anntation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogErrorParam {

    String[] value() default {};
}
