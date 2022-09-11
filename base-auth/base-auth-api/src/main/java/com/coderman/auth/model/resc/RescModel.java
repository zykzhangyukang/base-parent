package com.coderman.auth.model.resc;

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
@ApiModel(value="RescModel", description = "")
public class RescModel extends BaseModel {
    

    @ApiModelProperty(value = "资源id")
    private Integer rescId;

    @ApiModelProperty(value = "资源名称")
    private String rescName;

    @ApiModelProperty(value = "资源url")
    private String rescUrl;

    @ApiModelProperty(value = "资源所属系统")
    private String rescDomain;

    @ApiModelProperty(value = "请求方式")
    private String methodType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}