package com.coderman.sync.result;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 同步计划执行结果实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResultModel extends BaseModel {

    @ApiModelProperty("UUID")
    private String uuid;

    @ApiModelProperty(value = "同步计划uuid")
    private String planUuid;

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "来源")
    private String msgSrc;

    @ApiModelProperty(value = "MQ消息id")
    private String mqId;

    @ApiModelProperty(value = "同步消息id")
    private String msgId;

    @ApiModelProperty(value = "消息内容 ")
    private String msgContent;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String destProject;

    @ApiModelProperty(value = "同步内容")
    private String syncContent;

    @ApiModelProperty(value = "消息创建时间")
    private Date msgCreateTime;

    @ApiModelProperty(value = "同步时间")
    private Date syncTime;

    @ApiModelProperty(value = "同步状态")
    private String status;

    @ApiModelProperty(value = "失败消息")
    private String errorMsg;

    @ApiModelProperty(value = "重试次数")
    private Integer repeatCount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否同步到es")
    private boolean syncToEs;

}
