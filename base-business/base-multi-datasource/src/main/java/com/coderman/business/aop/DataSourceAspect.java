package com.coderman.business.aop;

import com.coderman.api.constant.AopConstant;
import com.coderman.business.annotation.DataSource;
import com.coderman.business.handler.DataSourceHolder;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author coderman
 */
@Component
@Aspect
@Order(value = AopConstant.DATA_SOURCE_ASPECT_ORDER)
public class DataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice {


    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        DataSourceHolder.clearDataSource();
    }

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {

        if(method.isAnnotationPresent(DataSource.class)){

            DataSourceHolder.setDataSource(method.getAnnotation(DataSource.class).name());

        }else {

            // 默认数据源
            DataSourceHolder.setDataSource(DataSource.class.getName());
        }

    }
}
