package com.coderman.sync.producer;

import com.coderman.service.service.BaseService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * rocketMQ生产者 - 顺序消息
 * @author zhangyukang
 */
@Component
public class RocketMQProducer extends BaseService {

    private DefaultMQProducer defaultMQProducer;

    @Value("${sync.rocketmq.producerGroup}")
    private String producerGroup;

    @Value("${sync.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${sync.rocketmq.instantName}")
    private String instantName;

    @Value("${sync.rocketmq.syncTopic}")
    private String syncTopic;

    @Value("${sync.rocketmq.sendMsgTimeoutMillis}")
    private int sendMsgTimeoutMillis;

    @Value("${sync.rocketmq.retryTimes}")
    private int retryTimes;

    @PostConstruct
    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(this.producerGroup);
        defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        defaultMQProducer.setInstanceName(this.instantName);
        defaultMQProducer.setCreateTopicKey(this.syncTopic);
        defaultMQProducer.setSendMsgTimeout(this.sendMsgTimeoutMillis);
        defaultMQProducer.setRetryTimesWhenSendFailed(this.retryTimes);
        defaultMQProducer.start();

        logger.info("rocketMQ初始化生产者完成[productGroup:{},instantName:{}]", this.producerGroup, this.instantName);
    }

    @PreDestroy
    public void destroy() {
        this.defaultMQProducer.shutdown();
        logger.info("rocketMQ生产者[productGroup:{},instantName:{}]销毁", this.producerGroup, this.instantName);
    }

    public SendResult send(Message message) throws Exception {
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
