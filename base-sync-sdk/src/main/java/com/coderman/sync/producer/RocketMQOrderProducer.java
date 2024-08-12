package com.coderman.sync.producer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * rocketMQ生产者
 * @author zhangyukang
 */
@Data
@Slf4j
public class RocketMQOrderProducer  {

    private DefaultMQProducer defaultMQProducer;

    private String producerOrderGroup;

    private String namesrvAddr;

    private String instantName;

    private String syncOrderTopic;

    private int sendMsgTimeoutMillis;

    private int retryTimes;

    public RocketMQOrderProducer() {
    }

    public RocketMQOrderProducer(String producerOrderGroup, String namesrvAddr, String instantName, String syncOrderTopic, int sendMsgTimeoutMillis, int retryTimes) {
        this.producerOrderGroup = producerOrderGroup;
        this.namesrvAddr = namesrvAddr;
        this.instantName = instantName;
        this.syncOrderTopic = syncOrderTopic;
        this.sendMsgTimeoutMillis = sendMsgTimeoutMillis;
        this.retryTimes = retryTimes;
    }

    public void start() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(this.producerOrderGroup);
        defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        defaultMQProducer.setCreateTopicKey(this.syncOrderTopic);
        defaultMQProducer.setInstanceName(this.instantName);
        defaultMQProducer.setSendMsgTimeout(this.sendMsgTimeoutMillis);
        defaultMQProducer.setRetryTimesWhenSendFailed(this.retryTimes);
        defaultMQProducer.start();

        log.info("rocketMQ初始化有序生产者完成[productOrderGroup:{},instantName:{}]", this.producerOrderGroup, this.instantName);
    }

    public void destroy() {
        this.defaultMQProducer.shutdown();
        log.info("rocketMQ有序生产者[productOrderGroup:{},instantName:{}]销毁", this.producerOrderGroup, this.instantName);
    }

    public SendResult send(String msg, String planCode, String key) throws Exception {
        Message message = new Message(syncOrderTopic, StringUtils.EMPTY, planCode, msg.getBytes(StandardCharsets.UTF_8));
        return this.defaultMQProducer.send(message, (mqs, m, arg) -> {
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
