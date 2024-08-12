package com.coderman.sync.config;

import com.coderman.sync.producer.ActiveMQProducer;
import com.coderman.sync.properties.SyncProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;

import javax.jms.DeliveryMode;
import javax.jms.Session;

/**
 * @author ：zhangyukang
 * @date ：2024/08/12 16:00
 */
@Configuration
@EnableConfigurationProperties({SyncProperties.class})
@ConditionalOnProperty(prefix = "sync", name = "mq", havingValue = "activemq")
public class ActiveMQAutoConfiguration {

    @Bean
    public ActiveMQProducer activeMQProducer() {
        return new ActiveMQProducer();
    }

    /**
     * 连接池的连接工厂，优化Mq的性能
     *
     * @param activeMqConnectionFactory
     * @return
     */
    @Bean
    public PooledConnectionFactory pooledSyncConnectionFactory(@NonNull ActiveMQConnectionFactory activeMqConnectionFactory) {
        PooledConnectionFactory cachingConnectionFactory = new PooledConnectionFactory(activeMqConnectionFactory);
        cachingConnectionFactory.setMaxConnections(4);
        return cachingConnectionFactory;
    }

    /**
     * activeMQ连接工厂
     *
     * @return
     */
    @Bean
    public ActiveMQConnectionFactory activeMqSyncConnectionFactory(SyncProperties syncProperties) {

        SyncProperties.ActiveMQ properties = syncProperties.getActivemq();
        return new ActiveMQConnectionFactory(properties.getUsername(), properties.getPassword(), properties.getBrokerUrl());
    }

    @Bean
    public JmsTemplate jmsSyncTemplate(@Qualifier(value = "pooledSyncConnectionFactory") PooledConnectionFactory pooledConnectionFactory,
                                       SyncProperties syncProperties) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(pooledConnectionFactory);
        jmsTemplate.setDefaultDestinationName(syncProperties.getActivemq().getQueueName());
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        jmsTemplate.setSessionTransacted(false);
        return jmsTemplate;
    }
}
