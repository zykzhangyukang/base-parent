package com.coderman.sync.plan;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlanModel extends BaseModel {

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "源数据库")
    private String srcDb;

    @ApiModelProperty(value = "目标数据库")
    private String destDb;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String destProject;

    @ApiModelProperty(value = "计划内容")
    private String planContent;

    @ApiModelProperty(value = "计划状态")
    private String status;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
