package com.coderman.auth.dao.user;

import com.coderman.auth.model.user.UserRoleExample;
import com.coderman.auth.model.user.UserRoleModel;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserRoleDAO extends BaseDAO<UserRoleModel, UserRoleExample> {


    /**
     * 新增用户角色关联
     * @param userId
     * @param assignedIdList
     */
    void insertBatchByUserId(@RequestParam(value = "userId") Integer userId, @Param(value = "roleIds") List<Integer> assignedIdList);

    /**
     * 新增角色用户关联
     * @param roleId
     * @param assignedIdList
     */
    void insertBatchByRoleId(@RequestParam(value = "roleId") Integer roleId, @Param(value = "userIds") List<Integer> assignedIdList);
}