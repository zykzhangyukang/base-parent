package com.coderman.api.constant;

/**
 * 顺序常量
 *
 * @author coderman
 * @date 2022/8/7 11:26
 */
public interface AopConstant {


    /**
     * 切面顺序
     */

    // 权限拦截
    int AUTH_ASPECT_ORDER = 1;


    // 参数校验
    int VALID_ASPECT_ORDER = 2;


    // 响应过滤
    int RESULT_ASPECT_ORDER = 2300;
}
