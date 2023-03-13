package com.coderman.sync.message;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class MqMessageModel extends BaseModel {

    @ApiModelProperty(value = "rocketmq消息id")
    private String mid;

    @ApiModelProperty(value = "消息内容")
    private String msgContent;

    @ApiModelProperty(value = "源系统")
    private String srcProject;

    @ApiModelProperty(value = "目标系统")
    private String destProject;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "消息发送时间")
    private Date sendTime;

    @ApiModelProperty(value = "消息ack时间")
    private Date ackTime;

    @ApiModelProperty(value = "发送状态")
    private String sendStatus;

    @ApiModelProperty(value = "处理状态")
    private String dealStatus;

    @ApiModelProperty(value = "处理次数")
    private Integer dealCount;
}
