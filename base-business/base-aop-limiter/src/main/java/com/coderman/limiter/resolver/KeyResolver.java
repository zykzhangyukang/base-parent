package com.coderman.limiter.resolver;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuwenbo1
 * @since 1.0.0
 */
public interface KeyResolver {
    /**
     * 具体限流规则
     *
     * @param request request
     * @param pjp
     * @return request
     */
    String resolve(HttpServletRequest request, ProceedingJoinPoint pjp);
}
