package com.coderman.sync.aop;

import com.coderman.api.constant.AopConstant;
import com.coderman.sync.util.SyncUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author coderman
 */
@Aspect
@Component
@Lazy(value = false)
@Order(value = AopConstant.SYNC_ASPECT_ORDER)
public class SyncAspect {



    @Around("execution(* com.coderman..timer..execute(..))" +
            "|| execution(* com.coderman..jobhandler..execute(..))" +
            "|| (execution(* com.coderman..controller..*(..)) && !execution(* com.coderman..BaseController.*(..)))")
    public Object sync(ProceedingJoinPoint joinPoint) throws Throwable{


        // 清理线程变量
        SyncUtil.clear();


        // 处理业务
        Object result = joinPoint.proceed();


        // 提交mq消息
        SyncUtil.submit();

        return result;
    }
}
