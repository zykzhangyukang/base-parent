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

@Component
public class RocketMQOrderProducer extends BaseService {

    private DefaultMQProducer defaultMQProducer;

    @Value("${sync.rocketmq.producerOrderGroup}")
    private String producerOrderGroup;

    @Value("${sync.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${sync.rocketmq.instantName}")
    private String instantName;

    @Value("${sync.rocketmq.syncOrderTopic}")
    private String syncOrderTopic;

    @Value("${sync.rocketmq.sendMsgTimeoutMillis}")
    private int sendMsgTimeoutMillis;

    @Value("${sync.rocketmq.retryTimes}")
    private int retryTimes;

    @PostConstruct
    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(this.producerOrderGroup);
        defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        defaultMQProducer.setCreateTopicKey(this.syncOrderTopic);
        defaultMQProducer.setInstanceName(this.instantName);
        defaultMQProducer.setSendMsgTimeout(this.sendMsgTimeoutMillis);
        defaultMQProducer.setRetryTimesWhenSendFailed(this.retryTimes);
        defaultMQProducer.start();

        logger.info("rocketMQ初始化有序生产者完成[productOrderGroup:{},instantName:{}]", this.producerOrderGroup, this.instantName);
    }

    @PreDestroy
    public void destroy() {
        this.defaultMQProducer.shutdown();
        logger.info("rocketMQ有序生产者[productOrderGroup:{},instantName:{}]销毁", this.producerOrderGroup, this.instantName);
    }

    public SendResult send(Message message, String key) throws Exception {

        return this.defaultMQProducer.send(message, (mqs, msg, arg) -> {
            int select = Math.abs(arg.hashCode());
            if(select <0){
                select = 0;
            }
            return mqs.get(select % mqs.size());
        }, key);

    }

    public DefaultMQProducer getDefaultMQProducer() {
        return defaultMQProducer;
    }

    public void setDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    public String getProducerOrderGroup() {
        return producerOrderGroup;
    }

    public void setProducerOrderGroup(String producerOrderGroup) {
        this.producerOrderGroup = producerOrderGroup;
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

    public String getSyncOrderTopic() {
        return syncOrderTopic;
    }

    public void setSyncOrderTopic(String syncOrderTopic) {
        this.syncOrderTopic = syncOrderTopic;
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
