package com.coderman.sync.config;

import com.coderman.sync.producer.RocketMQOrderProducer;
import com.coderman.sync.producer.RocketMQProducer;
import com.coderman.sync.properties.SyncProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：zhangyukang
 * @date ：2024/08/12 16:22
 */
@Configuration
@EnableConfigurationProperties({SyncProperties.class})
@ConditionalOnProperty(prefix = "sync", name = "mq", havingValue = "rocketmq")
public class RocketMQAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public RocketMQOrderProducer rocketMQOrderProducer(SyncProperties syncProperties){

        SyncProperties.RocketMQ properties = syncProperties.getRocketmq();

        return new RocketMQOrderProducer(
                properties.getProducerOrderGroup(),
                properties.getNamesrvAddr(),
                properties.getInstantName(),
                properties.getSyncOrderTopic(),
                properties.getSendMsgTimeoutMillis(),
                properties.getRetryTimes()
        );
    }

    @Bean(initMethod = "start",destroyMethod = "destroy")
    public RocketMQProducer rocketMQProducer(SyncProperties syncProperties){

        SyncProperties.RocketMQ properties = syncProperties.getRocketmq();

        return new RocketMQProducer(
                properties.getProducerGroup(),
                properties.getNamesrvAddr(),
                properties.getInstantName(),
                properties.getSyncTopic(),
                properties.getSendMsgTimeoutMillis(),
                properties.getRetryTimes()
        );
    }
}
