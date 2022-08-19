package com.coderman.auth.constant;


import com.coderman.api.anntation.Constant;
import com.coderman.api.anntation.ConstantList;

/**
 * @author coderman
 * @Title: 功能常量
 * @Description: TOD
 * @date 2022/5/311:18
 */
@Constant
public interface FuncConstant {

    /**
     * 项目最顶级的功能父级id
     */
    Integer func_root_parent_id = 0;

    /**
     * 功能类型
     */
    String func_type_group = "func_type_group";

    @ConstantList(group = func_type_group, name = "目录")
    String func_type_dir = "dir";

    @ConstantList(group = func_type_group, name = "功能")
    String func_type_func = "func";

}
