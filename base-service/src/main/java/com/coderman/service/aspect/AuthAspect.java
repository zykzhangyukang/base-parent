package com.coderman.service.aspect;

import com.alibaba.fastjson.JSON;
import com.coderman.api.constant.AopConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.vo.AuthUserVO;
import com.coderman.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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


    /**
     * 不拦截的接口
     */
    public static final List<String> whitelistUrl = new ArrayList<>();


    /**
     * 保存token与用户的关系
     */
    public static final Map<String, AuthUserVO> AUTH_USER_MAP = new HashMap<>();


    /**
     * 资源与功能对应关系
     */
    public static final Map<String, List<String>> allSystemResourceFunc = new HashMap<>();


    @Autowired
    public RedisService redisService;


    @PostConstruct
    public void init() {
        whitelistUrl.addAll(Arrays.asList("/auth/user/login", "/auth/check/code"));
    }


    @Pointcut("(execution(* com.coderman..controller..*(..)))")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();
        HttpServletResponse response = Objects.requireNonNull(servletRequestAttributes).getResponse();


        if (whitelistUrl.contains(request.getServletPath())) {
            return point.proceed();
        }

        // 访问令牌
        String token = request.getHeader("Authorization");

        if (StringUtils.isBlank(token)) {

            assert response != null;
            response.setStatus(ResultConstant.RESULT_CODE_401);
            return null;
        }


        AuthUserVO authUserVO = AUTH_USER_MAP.get(token);
        if (authUserVO == null || authUserVO.getExpiredTime() < System.currentTimeMillis()) {


            // redis查询token
            String authJson = this.redisService.getString(token, RedisDbConstant.REDIS_DB_AUTH);
            if (StringUtils.isBlank(authJson)) {

                assert response != null;
                response.setStatus(ResultConstant.RESULT_CODE_401);
                return null;
            } else {


                authUserVO = JSON.parseObject(authJson, AuthUserVO.class);
                if (authUserVO != null) {

                    String username = authUserVO.getUsername();

                    // 权限系统根据用户名查询用户信息


                }

            }


        }


        return point.proceed();
    }


}
