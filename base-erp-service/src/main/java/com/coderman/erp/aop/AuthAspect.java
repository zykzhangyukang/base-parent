package com.coderman.erp.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coderman.api.constant.AopConstant;
import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.vo.ResultVO;
import com.coderman.erp.api.RescApi;
import com.coderman.erp.api.UserApi;
import com.coderman.erp.config.AuthErpConfig;
import com.coderman.erp.util.AuthUtil;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.service.util.HttpContextUtil;
import com.coderman.service.util.SpringContextUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 权限拦截器
 *
 * @author coderman
 * @date 2022/8/7 11:25
 */
@Aspect
@Component
@Order(value = AopConstant.AUTH_ASPECT_ORDER)
@Lazy(value = false)
@Slf4j
public class AuthAspect {

    private UserApi userApi;
    private RescApi rescApi;

    private final RestTemplate restTemplate;
    private final AuthErpConfig authErpConfig;
    /**
     * 白名单接口
     */
    public static List<String> whiteListUrl = new ArrayList<>();
    /**
     * 资源url与功能关系
     */
    public static Map<String, Set<Integer>> systemAllResourceMap = new HashMap<>();
    /**
     * 无需拦截的url且有登录信息
     */
    public static List<String> unFilterHasLoginInfoUrl = new ArrayList<>();


    public AuthAspect(@Autowired(required = false) UserApi userApi, @Autowired(required = false) RescApi rescApi,
                                   @Autowired AuthErpConfig authErpConfig,@Autowired RestTemplateBuilder restTemplateBuilder) {
        this.userApi = userApi;
        this.rescApi = rescApi;
        this.authErpConfig = authErpConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * 保存token与用户的关系
     */
    public static final Cache<String, AuthUserVO> userTokenCacheMap = CacheBuilder.newBuilder()
            //设置缓存初始大小
            .initialCapacity(10)
            //最大值
            .maximumSize(500)
            //多线程并发数
            .concurrencyLevel(5)
            //过期时间，写入后5分钟过期
            .expireAfterWrite(5, TimeUnit.MINUTES)
            // 过期监听
            .removalListener((RemovalListener<String, AuthUserVO>) removalNotification -> {
                log.debug("过期会话缓存清除 token:{} is removed cause:{}", removalNotification.getKey(), removalNotification.getCause());
            })
            .recordStats()
            .build();


    @PostConstruct
    public void init() {

        // 项目名
        String project = System.getProperty("domain");

        // 白名单URL
        whiteListUrl.addAll(Arrays.asList("/auth/user/login", "/auth/user/logout", "/auth/api/resc/all", "/auth/api/user/info", "/auth/const/all"));

        // 无需拦截有会话信息URL
        unFilterHasLoginInfoUrl.addAll(Arrays.asList("/auth/user/info", "/auth/user/refresh/login", "/auth/user/pull/notify"));

        // 刷新系统资源
        refreshSystemAllRescMap(project);
    }

    /**
     * 刷新系统资源
     *
     * @param project 域名
     */
    private void refreshSystemAllRescMap(String project) {

        try {

            if (rescApi == null) {

                rescApi = SpringContextUtil.getBean(RescApi.class);
            }

            systemAllResourceMap = this.rescApi.getSystemAllRescMap(project).getResult();

        } catch (Exception e) {

            this.getAllAuthByHttp(project);
        }

    }


    @Pointcut("(execution(* com.coderman..controller..*(..)))")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();

        String path = request.getServletPath();

        // 白名单直接放行
        if (whiteListUrl.contains(path)) {

            return point.proceed();
        }

        // 访问令牌
        String token = request.getHeader(CommonConstant.USER_TOKEN_NAME);

        if (StringUtils.isBlank(token)) {

            response.setStatus(ResultConstant.RESULT_CODE_401);
            return null;
        }

        // 系统不存在的资源直接返回
        if (!systemAllResourceMap.containsKey(path) && !unFilterHasLoginInfoUrl.contains(path)) {

            response.setStatus(ResultConstant.RESULT_CODE_404);
            return null;
        }

        // 用户信息
        AuthUserVO authUserVO = null;
        try {
            authUserVO = userTokenCacheMap.get(token, () -> {

                log.debug("尝试从redis中获取用户信息结果.token:{}", token);

                try {

                    if (userApi == null) {

                        userApi = SpringContextUtil.getBean(UserApi.class);
                    }

                    return userApi.getUserByToken(token).getResult();

                } catch (Exception e) {

                    return getUserByHttp(token);

                }
            });
        } catch (Exception e) {

            log.error("尝试从redis中获取用户信息结果失败:{}", e.getMessage());
        }


        if (authUserVO == null || System.currentTimeMillis() > authUserVO.getExpiredTime()) {

            userTokenCacheMap.invalidate(token);
            assert response != null;
            response.setStatus(ResultConstant.RESULT_CODE_401);
            return null;
        }


        // 不需要过滤的url且有登入信息,设置会话后直接放行
        if (unFilterHasLoginInfoUrl.contains(path)) {

            AuthUtil.setCurrent(authUserVO);
            return point.proceed();
        }

        // 验证用户权限
        List<Integer> myRescIds = authUserVO.getRescIdList();
        Set<Integer> rescIds = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(systemAllResourceMap.get(path))) {
            rescIds = new HashSet<>(systemAllResourceMap.get(path));
        }


        if (CollectionUtils.isNotEmpty(myRescIds)) {

            for (Integer rescId : rescIds) {
                if (myRescIds.contains(rescId)) {

                    AuthUtil.setCurrent(authUserVO);
                    return point.proceed();
                }
            }

        }


        assert response != null;
        response.setStatus(ResultConstant.RESULT_CODE_403);
        return null;
    }

    private void checkAuthErpConfig(){
        Assert.notNull(authErpConfig.getAuthSecurityCode(), "缺少权限系统配置 {authSecurityCode}");
        Assert.notNull(authErpConfig.getAuthServerArr(), "缺少权限系统配置 {authServerArr}");
    }

    private AuthUserVO getUserByHttp(String token) {

        this.checkAuthErpConfig();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add(CommonConstant.USER_TOKEN_NAME, token);
        httpHeaders.add(CommonConstant.AUTH_SECURITY_NAME, authErpConfig.getAuthSecurityCode());

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> restRequest = new HttpEntity<>(paramMap, httpHeaders);

        // 请求auth获取用户信息
        JSONObject jsonObject = null;
        String[] authUrlArr = authErpConfig.getAuthServerArr().split(",");


        for (String authUrl : authUrlArr) {

            try {

                jsonObject = restTemplate.postForObject("http://" + authUrl + "/auth/api/user/info", restRequest, JSONObject.class);

                if (jsonObject != null) {

                    log.info("获取用户信息,请求权限系统成功. authServer:{}", authUrl);
                    break;
                }

            } catch (Exception e) {
                log.error("请求权限系统获取用户失败：token:" + token + ",url:http://" + authUrl + "/auth/api/user/info");
            }
        }


        if (jsonObject == null) {

            log.error("请求权限系统获取用户信息失败，用户为空");
            return null;
        }

        AuthUserVO authUserVO = jsonObject.getObject("result", AuthUserVO.class);
        if (authUserVO == null) {

            log.error("请求权限系统获取用户信息失败，返回信息:{}", jsonObject.toJSONString());
            return null;
        }

        return authUserVO;
    }


    @SuppressWarnings("all")
    private void getAllAuthByHttp(String domain) {

        this.checkAuthErpConfig();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(CommonConstant.AUTH_SECURITY_NAME, authErpConfig.getAuthSecurityCode());
        paramMap.add("domain", domain);
        HttpEntity<MultiValueMap<String, String>> restRequest = new HttpEntity<>(paramMap, httpHeaders);

        // 请求auth获取用户信息
        ResultVO<?> resultVO = null;
        String[] authUrlArr = authErpConfig.getAuthServerArr().split(",");

        for (String authUrl : authUrlArr) {

            // 请求url
            final String url = "http://" + authUrl + "/auth/api/resc/all";

            try {

                resultVO = restTemplate.postForObject(url, restRequest, ResultVO.class);

                if (resultVO != null) {

                    log.info("初始化系统资源,请求权限系统成功. 权限节点:{}", url);
                    break;
                }

            } catch (Exception e) {

                log.error("请求权限系统获取资源失败,权限节点:{}", url);
            }
        }

        if (resultVO == null || !ResultConstant.RESULT_CODE_200.equals(resultVO.getCode())) {

            log.error("请求权限系统获取非200:/auth/api/resc/all. resultVO:{}", JSON.toJSONString(resultVO));
            return;
        }

        systemAllResourceMap = (Map<String, Set<Integer>>) resultVO.getResult();
    }


}
