package com.coderman.service.aspect;

import com.alibaba.fastjson.JSONObject;
import com.coderman.api.constant.AopConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.vo.AuthUserVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.api.RescApi;
import com.coderman.service.api.UserApi;
import com.coderman.service.util.AuthUtil;
import com.coderman.service.util.SpringContextUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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


    @Autowired(required = false)
    private UserApi userApi;


    @Autowired(required = false)
    private RescApi rescApi;


    /**
     * 不拦截的接口
     */
    public static  List<String> whitelistUrl = new ArrayList<>();


    /**
     * 保存token与用户的关系
     */
    public static  Map<String, AuthUserVO> AUTH_USER_MAP = new HashMap<>();


    /**
     * 资源与功能资源id
     */
    public static  Map<String, Set<Integer>> systemAllResourceMap = new HashMap<>();


    /**
     * 不需要过滤的url且有登入信息
     */
    public static  List<String> unFilterHasLoginInfoUrlList = new ArrayList<>();


    @PostConstruct
    public void init() {

        // 项目名
        String project = System.getProperty("domain");

        // 白名单
        whitelistUrl.addAll(Arrays.asList("/auth/user/login", "/auth/check/code"));

        // 不需要过滤的url且有登入信息
        unFilterHasLoginInfoUrlList.addAll(Arrays.asList("/auth/user/info","/auth/const/all"));

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

            if(rescApi == null){

                rescApi = SpringContextUtil.getBean(RescApi.class);
            }

            systemAllResourceMap = this.rescApi.getSystemAllRescMap(project).getResult();

        }catch (Exception e){

            this.getAllAuthByHttp(project);
        }

    }



    @Pointcut("(execution(* com.coderman..controller..*(..)))")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();


        String path = request.getServletPath();

        // 白名单直接放行
        if (whitelistUrl.contains(path)) {
            return point.proceed();
        }

        // 访问令牌
        String token = request.getHeader("Authorization");

        if (StringUtils.isBlank(token)) {

            assert response != null;
            response.setStatus(ResultConstant.RESULT_CODE_401);
            return null;
        }

        // 系统不存在的资源直接返回
        if (!systemAllResourceMap.containsKey(path) && !unFilterHasLoginInfoUrlList.contains(path)) {


            assert response != null;
            response.setStatus(ResultConstant.RESULT_CODE_404);
            return null;
        }

        // 用户信息
        AuthUserVO authUserVO = AUTH_USER_MAP.get(token);

        if (authUserVO == null || authUserVO.getExpiredTime() < System.currentTimeMillis()) {

            try {

                if (userApi == null) {
                    userApi = SpringContextUtil.getBean(UserApi.class);
                }

                authUserVO = this.userApi.getUserByToken(token).getResult();

            } catch (Exception e) {

                authUserVO = this.getUserByHttp(token);
            }

            if (null != authUserVO) {

                authUserVO.setExpiredTime(System.currentTimeMillis() + 1000 * 60 * 30);
                AUTH_USER_MAP.put(token, authUserVO);
            }
        }

        if (authUserVO == null) {

            AUTH_USER_MAP.remove(token);
            assert response != null;
            response.setStatus(ResultConstant.RESULT_CODE_401);
            return null;
        }


        // 不需要过滤的url且有登入信息,设置会话后直接放行
        if (unFilterHasLoginInfoUrlList.contains(path)) {

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

    private AuthUserVO getUserByHttp(String token) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("securityCode", "codeXXXXXX");
        paramMap.add("Authorization", token);

        HttpEntity<MultiValueMap<String, String>> restRequest = new HttpEntity<>(paramMap, httpHeaders);

        // 请求auth获取用户信息
        JSONObject jsonObject = null;
        String[] authUrlArr = "127.0.0.1:8081".split(",");


        for (String authUrl : authUrlArr) {

            try {

                jsonObject = restTemplate.postForObject("http://" + authUrl + "/api/user/info", restRequest, JSONObject.class);

                if (jsonObject != null) {

                    break;
                }

            } catch (Exception e) {
                log.error("请求权限系统获取用户失败：token:" + token + ",url:http://" + authUrl + "/api/user/info", e);
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


    @SuppressWarnings("unchecked")
    private void getAllAuthByHttp(String domain) {


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("securityCode", "codeXXXXXX");
        paramMap.add("domain", domain);

        HttpEntity<MultiValueMap<String, String>> restRequest = new HttpEntity<>(paramMap, httpHeaders);

        // 请求auth获取用户信息
        ResultVO<Map<String, Set<Integer>>> resultVO = null;
        String[] authUrlArr = "127.0.0.1:8081".split(",");


        for (String authUrl : authUrlArr) {

            try {

                resultVO = restTemplate.postForObject("http://" + authUrl + "/api/resc/all", restRequest, ResultVO.class);

                if (resultVO != null) {
                    break;
                }

            } catch (Exception e) {
                log.error("请求权限系统获取用户失败,http://" + authUrl + "/api/resc/all", e);
            }
        }

        if (resultVO == null || !ResultConstant.RESULT_CODE_200.equals(resultVO.getCode())) {

            log.error("请求权限系统获取资源失败:/api/resc/all");
            return;
        }

        systemAllResourceMap = resultVO.getResult();
    }


}
