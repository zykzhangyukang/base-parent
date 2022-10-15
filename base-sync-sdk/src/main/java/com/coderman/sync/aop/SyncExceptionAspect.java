package com.coderman.sync.aop;

import com.coderman.api.constant.AopConstant;
import com.coderman.service.service.BaseService;
import com.coderman.sync.util.SyncUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(value = AopConstant.SYNC_EXCEPTION_ASPECT_ORDER)
public class SyncExceptionAspect extends BaseService {


    @Pointcut("@within(org.springframework.stereotype.Service) && execution(* com.coderman..service..*(..))")
    public void serviceAspect(){
    }


    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinpoint, Throwable e){

        // 清理线程变量
        SyncUtil.clear();
    }
}
