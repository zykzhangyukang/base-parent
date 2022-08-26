package com.coderman.service.aspect;

import com.coderman.api.constant.AopConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 权限拦截器
 *
 * @author coderman
 * @date 2022/8/7 11:25
 */
@Aspect
@Component
@Slf4j
@Order(value = AopConstant.AUTH_ASPECT_ORDER)
public class AuthAspect {





}
