package com.coderman.dbqueue.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author coderman
 * @Title: TODO
 * @Description: TOD
 * @date 2022/6/1814:45
 */
@Component
public class QueueSpringContextUtil implements ApplicationContextAware, DisposableBean {


    private static ApplicationContext applicationContext = null;

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext属性未注入, 请在SpringBoot启动类中注册SpringContextHolder.");
        } else {
            return applicationContext;
        }
    }

    /**
     * 通过name获取 Bean
     *
     * @param name 类名称
     * @return 实例对象
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    @Override
    public void destroy() {
        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        QueueSpringContextUtil.applicationContext = applicationContext;
    }
}