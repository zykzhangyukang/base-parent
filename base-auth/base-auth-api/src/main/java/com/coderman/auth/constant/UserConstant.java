package com.coderman.auth.constant;


import com.coderman.api.anntation.ConstantList;

/**
 * @author coderman
 * @Title: 系统模块常量
 * @Description: TOD
 * @date 2022/2/2623:50
 */
public interface UserConstant {


    /**
     * 用户状态
     */
    String USER_STATUS_GROUP = "user_status_group";

    @ConstantList(group = USER_STATUS_GROUP, name = "禁用")
    Integer USER_STATUS_DISABLE = 0;

    @ConstantList(group = USER_STATUS_GROUP, name = "启用")
    Integer USER_STATUS_ENABLE = 1;

}
