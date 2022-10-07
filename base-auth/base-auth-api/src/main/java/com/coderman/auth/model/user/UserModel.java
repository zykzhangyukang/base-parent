package com.coderman.auth.model.user;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ApiModel(value="UserModel", description = "用户")
public class UserModel extends BaseModel {
    

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "部门编号")
    private String deptCode;

    @ApiModelProperty(value = "状态")
    private Integer userStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}