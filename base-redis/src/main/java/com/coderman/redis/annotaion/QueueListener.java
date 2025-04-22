package com.coderman.redis.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 10:40
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QueueListener {
    String queue();
    int maxRetries() default 3;
    long retryDelay() default 5000L;
    int threadCount() default 1;
}
