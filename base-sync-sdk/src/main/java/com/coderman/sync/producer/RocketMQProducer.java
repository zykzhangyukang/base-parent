package com.coderman.sync.producer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * rocketMQ生产者 - 顺序消息
 * @author zhangyukang
 */
@Data
@Slf4j
public class RocketMQProducer  {

    private DefaultMQProducer defaultMQProducer;

    private String producerGroup;

    private String namesrvAddr;

    private String instantName;

    private String syncTopic;

    private int sendMsgTimeoutMillis;

    private int retryTimes;

    public RocketMQProducer(String producerGroup, String namesrvAddr, String instantName, String syncTopic, int sendMsgTimeoutMillis, int retryTimes) {
        this.producerGroup = producerGroup;
        this.namesrvAddr = namesrvAddr;
        this.instantName = instantName;
        this.syncTopic = syncTopic;
        this.sendMsgTimeoutMillis = sendMsgTimeoutMillis;
        this.retryTimes = retryTimes;
    }

    public RocketMQProducer() {
    }

    public void start() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(this.producerGroup);
        defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        defaultMQProducer.setInstanceName(this.instantName);
        defaultMQProducer.setCreateTopicKey(this.syncTopic);
        defaultMQProducer.setSendMsgTimeout(this.sendMsgTimeoutMillis);
        defaultMQProducer.setRetryTimesWhenSendFailed(this.retryTimes);
        defaultMQProducer.start();

        log.info("rocketMQ初始化生产者完成[productGroup:{},instantName:{}]", this.producerGroup, this.instantName);
    }

    public void destroy() {
        this.defaultMQProducer.shutdown();
        log.info("rocketMQ生产者[productGroup:{},instantName:{}]销毁", this.producerGroup, this.instantName);
    }

    public SendResult send(String msg, String planCode) throws Exception {
        Message message = new Message(syncTopic, StringUtils.EMPTY, planCode, msg.getBytes(StandardCharsets.UTF_8));
        return this.defaultMQProducer.send(message);
    }

    public SendResult send(List<Message> messageList) throws Exception {
        return this.defaultMQProducer.send(messageList);
    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

    public void setDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getInstantName() {
        return instantName;
    }

    public void setInstantName(String instantName) {
        this.instantName = instantName;
    }

    public String getSyncTopic() {
        return syncTopic;
    }

    public void setSyncTopic(String syncTopic) {
        this.syncTopic = syncTopic;
    }

    public int getSendMsgTimeoutMillis() {
        return sendMsgTimeoutMillis;
    }

    public void setSendMsgTimeoutMillis(int sendMsgTimeoutMillis) {
        this.sendMsgTimeoutMillis = sendMsgTimeoutMillis;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
