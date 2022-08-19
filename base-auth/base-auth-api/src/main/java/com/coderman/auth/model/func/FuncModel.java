package com.coderman.auth.model.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value="FuncModel", description = "")
public class FuncModel extends BaseModel {
    

    @ApiModelProperty(value = "主键")
    private Integer funcId;

    @ApiModelProperty(value = "")
    private String funcName;

    @ApiModelProperty(value = "")
    private String funcKey;

    @ApiModelProperty(value = "")
    private String funcType;

    @ApiModelProperty(value = "")
    private String funcIcon;

    @ApiModelProperty(value = "")
    private Integer funcSort;

    @ApiModelProperty(value = "")
    private Boolean dirHide;

    @ApiModelProperty(value = "")
    private Integer parentId;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;
}