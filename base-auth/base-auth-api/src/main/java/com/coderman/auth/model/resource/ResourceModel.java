package com.coderman.auth.model.resource;

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
@ApiModel(value="ResourceModel", description = "")
public class ResourceModel extends BaseModel {
    

    @ApiModelProperty(value = "")
    private Integer resourceId;

    @ApiModelProperty(value = "")
    private String resourceName;

    @ApiModelProperty(value = "")
    private String resourceUrl;

    @ApiModelProperty(value = "")
    private String resourceDomain;

    @ApiModelProperty(value = "")
    private Date createTime;

    @ApiModelProperty(value = "")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String methodType;
}