package com.coderman.auth.dao.role;

import com.coderman.auth.model.role.RoleExample;
import com.coderman.auth.model.role.RoleModel;
import com.coderman.auth.vo.role.RoleVO;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDAO extends BaseDAO<RoleModel, RoleExample> {


    /**
     * 角色列表
     *
     * @param queryVO
     * @return
     */
    List<RoleVO> page(@Param(value = "queryVO") RoleVO queryVO);


    /**
     * 根据角色id查询用户
     * @param roleId
     * @return
     */
    List<String> selectUserByRoleId(@Param(value = "roleId") Integer roleId);

    /**
     * 查看用户拥有的角色
     *
     * @param userId
     * @return
     */
    List<RoleModel> selectUserRoleList(@Param(value = "userId") Integer userId);
}