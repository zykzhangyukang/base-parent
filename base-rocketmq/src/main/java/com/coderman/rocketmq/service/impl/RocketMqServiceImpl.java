package com.coderman.rocketmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coderman.rocketmq.constant.RocketMqSysConstant;
import com.coderman.rocketmq.service.RocketMqService;
import com.coderman.rocketmq.vo.BaseMqMessage;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RocketMqServiceImpl implements RocketMqService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqServiceImpl.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 同步消息
     * @param topic 主题
     * @param tag 标签
     * @param message 消息体
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseMqMessage> SendResult send(String topic, String tag, T message) {
        return send(topic + RocketMqSysConstant.DELIMITER + tag, message);
    }

    /**
     * 延迟消息
     * @param topic 主题
     * @param tag 标签
     * @param message 消息体
     * @param delayLevel 延迟等级
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseMqMessage> SendResult send(String topic, String tag, T message, int delayLevel) {
        return send(topic + RocketMqSysConstant.DELIMITER + tag, message, delayLevel);
    }


    /**
     * 构建目的地
     */
    private String buildDestination(String topic, String tag) {
        return topic + RocketMqSysConstant.DELIMITER + tag;
    }

    /**
     * 发送同步消息
     *
     * @param destination 目的地
     * @param message 消息内容
     * @param <T> 消息
     * @return 消息
     */
    private <T extends BaseMqMessage> SendResult send(String destination, T message) {

        // 更多的其它基础业务处理...
        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = rocketMQTemplate.syncSend(destination, sendMessage);

        // 此处为了方便查看给日志转了json，根据选择选择日志记录方式，例如ELK采集
        LOGGER.info("[{}]同步消息[{}]发送结果[{}]", destination, JSON.toJSONString(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }


    /**
     * 发送延迟消息
     * @param destination 目的地
     * @param message 消息内容
     * @param delayLevel 延迟等级
     * @param <T> 消息
     * @return 消息
     */
    private  <T extends BaseMqMessage> SendResult send(String destination, T message, int delayLevel) {

        Message<T> sendMessage = MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, message.getKey()).build();
        SendResult sendResult = rocketMQTemplate.syncSend(destination, sendMessage, 3000, delayLevel);

        LOGGER.info("[{}]延迟等级[{}]消息[{}]发送结果[{}]", destination, delayLevel, JSON.toJSONString(message), JSONObject.toJSON(sendResult));
        return sendResult;
    }
}
