package com.coderman.sync.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author coderman
 */
@ApiModel(value = "同步消息体对象")
public class MsgBody {

    @ApiModelProperty(value = "uuid")
    private String msgId;

    @ApiModelProperty(value = "消息内容")
    private String msg;

    @ApiModelProperty(value = "同步计划编号")
    private String planCode;

    @ApiModelProperty(value = "mq消息id")
    private Integer mqMessageId;


    public MsgBody(String msgId, String msg, String planCode, Integer mqMessageId) {
        this.msgId = msgId;
        this.msg = msg;
        this.planCode = planCode;
        this.mqMessageId = mqMessageId;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Integer getMqMessageId() {
        return mqMessageId;
    }

    public void setMqMessageId(Integer mqMessageId) {
        this.mqMessageId = mqMessageId;
    }
}
