package com.coderman.erp.util;

import com.coderman.api.constant.CommonConstant;
import com.coderman.erp.vo.AuthUserVO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthUtil {


    /**
     * 获取当前用户会话信息
     *
     * @return
     */
    public static AuthUserVO getCurrent() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        return (AuthUserVO) servletRequestAttributes.getRequest().getAttribute(CommonConstant.USER_SESSION_KEY);
    }


    /**
     * 设置当前会话
     *
     * @param authUserVO 用户信息
     */
    public static void setCurrent(AuthUserVO authUserVO) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        servletRequestAttributes.getRequest().setAttribute(CommonConstant.USER_SESSION_KEY, authUserVO);
    }
}
