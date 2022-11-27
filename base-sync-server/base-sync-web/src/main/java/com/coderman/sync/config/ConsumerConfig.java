package com.coderman.sync.config;

import com.coderman.sync.listener.RocketMqListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 消费者配置
 */
@Configuration
public class ConsumerConfig {

    @Resource
    private RocketMqListener rocketMqListener;


    @Bean(value = "defaultMQPushConsumer",initMethod = "start",destroyMethod = "shutdown")
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {

        DefaultMQPushConsumer mqPushConsumer =  new DefaultMQPushConsumer();
        mqPushConsumer.setNamesrvAddr("119.91.30.131:9876");
        mqPushConsumer.setInstanceName("syncServer");
        mqPushConsumer.setConsumerGroup("syncConsumeGroup");
        mqPushConsumer.subscribe("SyncTopic","*");
        mqPushConsumer.setMessageListener(rocketMqListener);
        mqPushConsumer.setConsumeThreadMin(40);
        mqPushConsumer.setConsumeThreadMax(128);
        return mqPushConsumer;
    }
}
