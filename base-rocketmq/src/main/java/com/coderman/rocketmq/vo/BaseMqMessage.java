package com.coderman.rocketmq.vo;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * @author coderman
 */
@Data
public abstract class BaseMqMessage {
    /**
     * 业务键，用于RocketMQ控制台查看消费情况
     */
    protected String key;
    /**
     * 发送消息来源，用于排查问题
     */
    protected String source = "";
    /**
     * 发送时间
     */
    protected Date sendTime = new Date();
    /**
     * 跟踪id，用于slf4j等日志记录跟踪id，方便查询业务链
     */
    protected String traceId = UUID.randomUUID().toString();
    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;
}
