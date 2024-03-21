package com.coderman.rocketmq.model;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author coderman
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ApiModel(value="MqMsgModel", description = "")
public class MqMsgModel extends BaseModel {
    
    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "消息id")
    private String mid;

    @ApiModelProperty(value = "消息标签")
    private String tag;

    @ApiModelProperty(value = "消息内容")
    private String msg;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "发送状态")
    private String sendStatus;

    @ApiModelProperty(value = "重试次数")
    private Integer retry;
}