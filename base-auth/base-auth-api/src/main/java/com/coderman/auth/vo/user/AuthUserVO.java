package com.coderman.auth.vo.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登入用户
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
}
