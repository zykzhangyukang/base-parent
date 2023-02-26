package com.coderman.sync.producer;

import com.coderman.service.service.BaseService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class RocketMQOrderProducer extends BaseService {

    private DefaultMQProducer defaultMQProducer;
    private String producerGroup;
    private String namesrvAddr;
    private String instantName;
    private String syncTopic;
    private int sendMsgTimeoutMillis;
    private int retryTimes;

    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer(this.producerGroup);
        defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        defaultMQProducer.setCreateTopicKey(this.syncTopic);
        defaultMQProducer.setInstanceName(this.instantName);
        defaultMQProducer.setSendMsgTimeout(this.sendMsgTimeoutMillis);
        defaultMQProducer.setRetryTimesWhenSendFailed(this.retryTimes);
        defaultMQProducer.start();

        logger.info("rocketMQ初始化有序生产者完成[productOrderGroup:{},instantName:{}]", this.producerGroup, this.instantName);
    }

    public void destroy() {
        this.defaultMQProducer.shutdown();
        logger.info("rocketMQ有序生产者[productOrderGroup:{},instantName:{}]销毁", this.producerGroup, this.instantName);
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
