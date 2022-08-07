package com.coderman.api.constant;

/**
 * 顺序常量
 *
 * @author coderman
 * @date 2022/8/7 11:26
 */
public interface AspectConstant {


    /**
     * 切面顺序
     */

    // 权限拦截
    Integer AUTH_ASPECT_ORDER = 1;


    // 参数校验
    Integer VALID_ASPECT_ORDER = 2;


    // 响应过滤
    Integer RESULT_ASPECT_ORDER = 2300;
}
