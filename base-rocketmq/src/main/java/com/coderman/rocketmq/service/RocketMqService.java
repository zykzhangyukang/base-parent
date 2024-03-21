package com.coderman.rocketmq.service;

import com.coderman.rocketmq.vo.BaseMqMessage;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @author coderman
 */
public interface RocketMqService {


    /**
     * 发送同步消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息体
     * @param <T> 消息
     * @return 消息
     */
    public <T extends BaseMqMessage> SendResult send(String topic, String tag, T message);


    /**
     * 发送延迟消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息体
     * @param delayLevel 延迟等级
     * @param <T> 消息
     * @return 消息
     */
    public <T extends BaseMqMessage> SendResult send(String topic, String tag, T message, int delayLevel);
}
