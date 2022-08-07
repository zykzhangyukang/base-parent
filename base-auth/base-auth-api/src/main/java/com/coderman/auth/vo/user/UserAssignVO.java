package com.coderman.auth.vo.user;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.model.role.RoleModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 用户分配对象
 * @Description: TOD
 * @date 2022/3/612:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAssignVO extends BaseModel {

    /**
     * 所有角色
     */
    private List<RoleModel> roleList;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户已经分配的角色id
     */
    private List<Integer> assignedIdList;
}
