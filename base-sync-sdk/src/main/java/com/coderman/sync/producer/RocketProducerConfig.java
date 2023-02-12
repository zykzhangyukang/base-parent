package com.coderman.sync.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sync.rocketmq")
@SuppressWarnings("all")
@Data
public class RocketProducerConfig {

    private String producerGroup;
    private String producerOrderGroup;

    private String namesrvAddr;
    private String instantName;

    private String producerTopic;
    private String producerOrderTopic;

    private int sendMsgTimeoutMillis;
    private int retryTimes;

    @Bean(value = "rocketMQProducer",initMethod = "init",destroyMethod = "destroy")
    public RocketMQProducer rocketMQProducer(){

        RocketMQProducer rocketMQProducer = new RocketMQProducer();
        rocketMQProducer.setProducerGroup(producerGroup);
        rocketMQProducer.setNamesrvAddr(namesrvAddr);
        rocketMQProducer.setInstantName(instantName);
        rocketMQProducer.setSyncTopic(producerTopic);
        rocketMQProducer.setSendMsgTimeoutMillis(sendMsgTimeoutMillis);
        rocketMQProducer.setRetryTimes(retryTimes);
        return rocketMQProducer;
    }

    @Bean(value = "rocketOrderMQProducer",initMethod = "init",destroyMethod = "destroy")
    public RocketMQOrderProducer rocketMQOrderProducer(){

        RocketMQOrderProducer rocketMQProducer = new RocketMQOrderProducer();
        rocketMQProducer.setProducerGroup(producerOrderGroup);
        rocketMQProducer.setNamesrvAddr(namesrvAddr);
        rocketMQProducer.setInstantName(instantName);
        rocketMQProducer.setSyncTopic(producerOrderTopic);
        rocketMQProducer.setSendMsgTimeoutMillis(sendMsgTimeoutMillis);
        rocketMQProducer.setRetryTimes(retryTimes);
        return rocketMQProducer;
    }

}