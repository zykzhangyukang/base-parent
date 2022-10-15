package com.coderman.business.aop;

import com.coderman.api.constant.AopConstant;
import com.coderman.business.handler.DataSourceHolder;
import com.coderman.business.handler.DynamicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Aspect
@Order(value = AopConstant.DATA_SOURCE_ASPECT_ORDER)
public class MultiDataSourceAspect  {


    @Around("@within(org.springframework.stereotype.Service) && execution(* com.coderman..service..*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{

        String packagePath = getMatcher("(com\\.coderman\\.[a-z]+)",joinPoint.getTarget().getClass().getPackage().getName());

        if(DynamicDataSource.getDatasourcePackageMap().containsKey(packagePath)){

            DataSourceHolder.setDataSource(DynamicDataSource.getDatasourcePackageMap().get(packagePath));
        }

        Object result = joinPoint.proceed();


        // 设置数据源为默认数据源
        DataSourceHolder.setDataSource(DynamicDataSource.getDefaultDataSourceName());

        return result;
    }


    public String getMatcher(String regex,String source){

        String result = StringUtils.EMPTY;
        Matcher matcher = Pattern.compile(regex).matcher(source);

        while (matcher.find()){
            result = matcher.group(1);
        }

        return result;

    }
}
