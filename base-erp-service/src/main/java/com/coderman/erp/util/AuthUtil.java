package com.coderman.erp.util;

import com.coderman.api.constant.CommonConstant;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.service.util.HttpContextUtil;

import javax.servlet.http.HttpServletRequest;

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
