package com.coderman.api.constant;

/**
 * 顺序常量
 *
 * @author coderman
 * @date 2022/8/7 11:26
 */
public interface AopConstant {


    /**
     * Controller开始层切面
     */

    // 权限拦截
    public static final int AUTH_ASPECT_ORDER = 100;


    // 参数校验
    public static final int VALID_ASPECT_ORDER = 300;


    /**
     * Service层切面
     */

    // 数据源切换
    public static final int DATA_SOURCE_ASPECT_ORDER = 1200;

    // 同步异常
    public static final int SYNC_EXCEPTION_ASPECT_ORDER = 1300;

    // 系统异常
    public static final int EXCEPTION_ASPECT_ORDER = 1400;


    // 同步系统
    /**
     * controller结束切面
     */
    public static final int SYNC_ASPECT_ORDER = 2100;

    // 响应过滤
    public static final int RESULT_ASPECT_ORDER = 2300;
}
