package com.coderman.auth.model.role;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ApiModel(value="RoleFuncModel", description = "")
public class RoleFuncModel extends BaseModel {
    

    @ApiModelProperty(value = "主键")
    private Integer roleFuncId;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "功能id")
    private Integer funcId;
}