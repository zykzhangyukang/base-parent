package com.coderman.limiter.resolver;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 1.0.0
 * 根据请求的uri进行限流
 */
public class UriKeyResolver implements KeyResolver {

    @Override
    public String resolve(HttpServletRequest request, ProceedingJoinPoint pjp) {
        return request.getRequestURI();
    }
}
