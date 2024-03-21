package com.coderman.swagger.annotation;

import java.lang.annotation.*;

/**
 * @author coderman
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiReturnIgnore {
}
