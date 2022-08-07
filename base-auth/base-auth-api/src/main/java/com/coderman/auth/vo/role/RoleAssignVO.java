package com.coderman.auth.vo.role;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.model.user.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 角色分配VO
 * @Description: TOD
 * @date 2022/3/2012:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleAssignVO extends BaseModel {

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 所有角色
     */
    private List<UserModel> userList;


    /**
     * 用户已经分配的用户id
     */
    private List<Integer> assignedIdList;
}
