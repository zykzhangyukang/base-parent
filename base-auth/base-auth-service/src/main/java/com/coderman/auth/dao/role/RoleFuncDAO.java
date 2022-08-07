package com.coderman.auth.dao.role;

import java.util.List;

import com.coderman.auth.model.role.RoleFuncModel;
import com.coderman.auth.model.role.RoleFuncModelExample;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

public interface RoleFuncDAO extends BaseDAO<RoleFuncModel,RoleFuncModelExample> {


    /**
     * 批量查询角色-功能关联
     * @param roleId
     * @param funcKeyList
     */
    void batchInsertByRoleId(@Param(value = "roleId") Integer roleId,@Param(value = "funcKeyList") List<String> funcKeyList);
}