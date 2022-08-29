package com.coderman.auth.constant;

import com.coderman.api.anntation.ConstList;
import com.coderman.api.anntation.Constant;

@Constant
public interface ProjectConstant {

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


    /**
     * 项目常量
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


}
