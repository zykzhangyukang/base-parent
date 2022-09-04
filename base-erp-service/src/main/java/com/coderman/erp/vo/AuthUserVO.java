package com.coderman.erp.vo;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 当前登入用户信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthUserVO extends BaseModel {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "登入token")
    private String token;

    @ApiModelProperty(value = "会话过期时间")
    private Long expiredTime;

    @ApiModelProperty(value = "资源id")
    private List<Integer> rescIdList;
}
