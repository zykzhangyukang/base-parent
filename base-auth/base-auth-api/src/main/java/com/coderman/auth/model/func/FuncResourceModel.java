package com.coderman.auth.model.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ApiModel(value="FuncResourceModel", description = "")
public class FuncResourceModel extends BaseModel {
    

    @ApiModelProperty(value = "")
    private Integer funcResourceId;

    @ApiModelProperty(value = "")
    private Integer funcId;

    @ApiModelProperty(value = "")
    private Integer resourceId;
}