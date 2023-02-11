package com.coderman.sync.config;

import com.coderman.sync.listener.RocketMqListener;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

//@Configuration
//@ConfigurationProperties(prefix = "sync.rocketmq")
@Data
public class ListenerConfig {

    @Resource
    private RocketMqListener rocketMqListener;

    private String namesrvAddr;

    private String instanceName;

    private String consumerGroup;

    private String topic;

    private Integer consumeThreadMin;

    private Integer consumeThreadMax;


    @Bean(value = "defaultMQPushConsumer", initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {

        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setNamesrvAddr(namesrvAddr);
        mqPushConsumer.setInstanceName(instanceName);
        mqPushConsumer.setConsumerGroup(consumerGroup);
        mqPushConsumer.subscribe(topic, "*");
        mqPushConsumer.setMessageListener(rocketMqListener);
        mqPushConsumer.setConsumeThreadMin(consumeThreadMin);
        mqPushConsumer.setConsumeThreadMax(consumeThreadMax);
        return mqPushConsumer;
    }
}
