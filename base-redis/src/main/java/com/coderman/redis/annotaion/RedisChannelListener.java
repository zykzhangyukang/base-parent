package com.coderman.redis.annotaion;

import java.lang.annotation.*;

/**
 * redis 发布定义监听者注解 用于标注在方法上 省去多余的注册操作
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisChannelListener {

    /**
     * 是否区分环境,默认区分环境
     */
    boolean envDiff() default true;


    /**
     * 实际通道名
     */
    String channelName() default "";


    /**
     * 接受的消息实体类名
     */
    Class<?> clazz() default String.class;
}

