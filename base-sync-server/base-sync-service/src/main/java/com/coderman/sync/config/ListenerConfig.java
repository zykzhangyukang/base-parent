package com.coderman.sync.config;

import com.coderman.sync.listener.RocketMqListener;
import com.coderman.sync.listener.RocketMqOrderListener;
import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConfigurationProperties(prefix = "sync.rocketmq")
@Data
public class ListenerConfig {

    @Resource
    private RocketMqListener rocketMqListener;

    @Resource
    private RocketMqOrderListener rocketMqOrderListener;

    private String namesrvAddr;

    private String instanceName;

    private String consumerGroup;

    private String consumerOrderGroup;

    private String topic;

    private String orderTopic;

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

    @Bean(value = "orderlyMQPushConsumer", initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer orderlyMQPushConsumer() throws MQClientException {

        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setInstanceName(instanceName);
        mqPushConsumer.setNamesrvAddr(namesrvAddr);
        mqPushConsumer.setConsumerGroup(consumerOrderGroup);
        mqPushConsumer.subscribe(orderTopic, "*");
        mqPushConsumer.setMessageListener(rocketMqOrderListener);
        mqPushConsumer.setConsumeThreadMin(consumeThreadMin);
        mqPushConsumer.setConsumeThreadMax(consumeThreadMax);
        return mqPushConsumer;
    }

}
