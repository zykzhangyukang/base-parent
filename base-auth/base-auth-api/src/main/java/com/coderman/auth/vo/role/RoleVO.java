package com.coderman.auth.vo.role;

import com.coderman.auth.model.role.RoleModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 角色VO
 * @Description: TOD
 * @date 2022/2/2711:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends RoleModel {

    /**
     * 用户信息
     */
    private List<String> userNameList;
}
