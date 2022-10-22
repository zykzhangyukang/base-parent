package com.coderman.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author coderman
 * @date 2022/6/2120:12
 */
@Slf4j
@Service
@Lazy(value = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException{

        if(clazz == null){
            return null;
        }

        return (T) applicationContext.getBean(clazz);
    }

    @SuppressWarnings("all")
    public static <T> T getBean(String beanName){

        if(beanName == null){
            return null;
        }

        return (T) applicationContext.getBean(beanName);
    }

    public static void clearHolder(){
        applicationContext = null;
    }


    /**
     * 发布事件
     * @param event
     */
    public static void publishEvent(ApplicationEvent event){
        if(applicationContext == null){
            return;
        }
        applicationContext.publishEvent(event);
    }


    @Override
    public void destroy() throws Exception {

    }
}
