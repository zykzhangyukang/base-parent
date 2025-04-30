package com.coderman.service.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

/**
 * @author coderman
 * @date 2022/6/2120:12
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, DisposableBean {

    @Getter
    private static volatile ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext){
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException{
        checkApplicationContext();
        return (T) applicationContext.getBean(clazz);
    }

    @SuppressWarnings("all")
    public static <T> T getBean(String beanName){
        checkApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }


    /**
     * 发布事件
     * @param event
     */
    public static void publishEvent(ApplicationEvent event){
        checkApplicationContext();
        applicationContext.publishEvent(event);
    }


    private static void checkApplicationContext(){
        if(applicationContext == null){
            throw new IllegalStateException("applicationContext未注入!");
        }
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

    /**
     * 刷新上下文的时候更新ioc容器,确保是最新的ioc
     * @param event 事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        if (ctx.getParent() == null) { // 只处理根容器
            SpringContextUtil.applicationContext = ctx;
            log.debug("ApplicationContext refreshed!");
        }
    }
}
