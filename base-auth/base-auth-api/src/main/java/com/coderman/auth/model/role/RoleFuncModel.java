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
    

    @ApiModelProperty(value = "")
    private Integer roleFuncId;

    @ApiModelProperty(value = "")
    private Integer roleId;

    @ApiModelProperty(value = "")
    private Integer funcId;
}