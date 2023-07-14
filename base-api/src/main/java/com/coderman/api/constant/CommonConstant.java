package com.coderman.api.constant;

/**
 * @author coderman
 * @Title: 公共的常量
 * @date 2022/5/2521:45
 */
public interface CommonConstant {

    /**
     * 基础项目包名称
     */
    String BASE_PACKAGE = "com.coderman";


    /**
     * 基础的dao包名称
     */
    String BASE_DAO_PACKAGE= "com.coderman.*.dao";

    /**
     * 用户会话key
     */
    String USER_SESSION_KEY = "auth.user.info";

    /**
     * 请求头Token名称
     */
    String USER_TOKEN_NAME = "Authorization";

    /**
     * 请求头权限安全码
     */
    String AUTH_SECURITY_NAME = "AuthSecurityCode";

    /**
     * 全局异常错误信息提示
     */
    String GLOBAL_FAIL_MSG = "Controller统一异常处理";

    /**
     * redis 分布式锁
     */
    String REDIS_GLOBAL_LOCK_KEY_CONSTANT = "redis_global_lock_";

    /**
     * 全局异常报错信息
     */
    public static final String GLOBAL_FAIL_MSG = " Controller统一异常处理";


    /**
     * 分页每页显示
     */
    Integer SYS_PAGE_SIZE = 30;
    String SYS_PAGE_LIMIT = "30000";
}
