package com.coderman.auth.model.role;

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
@ApiModel(value="RoleModel", description = "")
public class RoleModel extends BaseModel {
    

    @ApiModelProperty(value = "")
    private Integer roleId;

    @ApiModelProperty(value = "")
    private String roleName;

    @ApiModelProperty(value = "")
    private String roleDesc;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;
}