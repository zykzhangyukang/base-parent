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
@ApiModel(value="FuncRescModel", description = "")
public class FuncRescModel extends BaseModel {
    

    @ApiModelProperty(value = "主键")
    private Integer funcRescId;

    @ApiModelProperty(value = "功能id")
    private Integer funcId;

    @ApiModelProperty(value = "资源id")
    private Integer rescId;
}