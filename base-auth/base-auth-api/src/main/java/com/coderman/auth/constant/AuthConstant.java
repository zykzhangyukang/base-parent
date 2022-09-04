package com.coderman.auth.constant;


import com.coderman.api.anntation.Constant;
import com.coderman.api.anntation.ConstList;

/**
 * @author coderman
 * @Title: 系统模块常量
 * @Description: TOD
 * @date 2022/2/2623:50
 */
@Constant
public interface AuthConstant {

    /**
     * 用户会话redis key 前缀
     */
    String AUTH_TOKEN_NAME = "auth:token:";

    /**
     * 用户状态
     */
    String USER_STATUS_GROUP = "user_status_group";

    @ConstList(group = USER_STATUS_GROUP, name = "禁用")
    Integer USER_STATUS_DISABLE = 0;

    @ConstList(group = USER_STATUS_GROUP, name = "启用")
    Integer USER_STATUS_ENABLE = 1;

    /**
     * 项目常量
     */
    String project_domain = "project_domain";


    @ConstList(group = project_domain, name = "权限系统")
    String project_domain_auth = "auth";

    @ConstList(group = project_domain, name = "网关服务")
    String project_domain_gateway = "gateway";

    @ConstList(group = project_domain, name = "搜索服务")
    String project_domain_search = "search";

    @ConstList(group = project_domain, name = "文件服务")
    String project_domain_file = "file";

    @ConstList(group = project_domain, name = "测试服务")
    String project_domain_demo = "demo";


    /**
     * 方法常量
     */
    String method_type = "method_type";

    @ConstList(group = method_type, name = "GET请求")
    String method_type_get = "get";

    @ConstList(group = method_type, name = "POST请求")
    String method_type_post = "post";

    @ConstList(group = method_type, name = "DELETE请求")
    String method_type_delete = "delete";

    @ConstList(group = method_type, name = "PUT请求")
    String method_type_put = "put";


    /**
     * 项目最顶级的功能父级id
     */
    Integer func_root_parent_id = 0;

    /**
     * 功能类型
     */
    String func_type_group = "func_type_group";

    @ConstList(group = func_type_group, name = "目录")
    String func_type_dir = "dir";

    @ConstList(group = func_type_group, name = "功能")
    String func_type_func = "func";
}
