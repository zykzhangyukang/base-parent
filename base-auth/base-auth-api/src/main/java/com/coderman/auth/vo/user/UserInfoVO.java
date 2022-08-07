package com.coderman.auth.vo.user;

import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.vo.func.MenuVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 用户信
 * @Description: TOD
 * @date 2022/5/312:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoVO extends UserModel {

    @ApiModelProperty(value = "菜单信息")
    private List<MenuVO> menus;

    @ApiModelProperty(value = "功能key")
    private List<String> funcKeys;

    @ApiModelProperty(value = "角色信息")
    private List<String> roles;

    @ApiModelProperty(value = "部门名称")
    private String deptName;
}
