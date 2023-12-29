package com.coderman.erp.util;

import com.alibaba.fastjson.JSON;
import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.RedisDbConstant;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.redis.service.RedisService;
import com.coderman.service.util.HttpContextUtil;
import com.coderman.service.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@Slf4j
public class AuthUtil {


    /**
     * 获取当前用户会话信息
     *
     * @return
     */
    public static AuthUserVO getCurrent() {

        HttpServletRequest httpServletRequest = HttpContextUtil.getHttpServletRequest();
        Object obj = httpServletRequest.getAttribute(CommonConstant.USER_SESSION_KEY);

        if (obj instanceof AuthUserVO) {

            return (AuthUserVO) obj;

        } else{

            // 如果用户的token存在，则尝试从redis中获取
            String token = httpServletRequest.getHeader(CommonConstant.USER_TOKEN_NAME);
            if (StringUtils.isNotBlank(token)) {

                AuthUserVO authUserVO = null;
                try {

                    RedisService redisService = SpringContextUtil.getBean(RedisService.class);
                    authUserVO = redisService.getObject(CommonConstant.USER_TOKEN_NAME + token, AuthUserVO.class, RedisDbConstant.REDIS_DB_AUTH);

                } catch (Exception e) {
                    log.error("从redis获取用户信息失败！error:{}", e.getMessage(), e);
                }

                if(authUserVO!=null){
                    log.warn("从redis中获取用户信息成功:authUserVO:{}", JSON.toJSONString(authUserVO));
                }

                return authUserVO;
            }

        }

        return null;
    }


    /**
     * 设置当前会话
     *
     * @param authUserVO 用户信息
     */
    public static void setCurrent(AuthUserVO authUserVO) {
        HttpServletRequest httpServletRequest = HttpContextUtil.getHttpServletRequest();
        httpServletRequest.setAttribute(CommonConstant.USER_SESSION_KEY, authUserVO);
    }
}
