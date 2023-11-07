package com.coderman.sync.producer;

import com.coderman.service.service.BaseService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;

/**
 * ActiveMQ 生产者
 *
 * @author ：zhangyukang
 * @date ：2023/11/06 9:43
 */
@Component
@ConditionalOnProperty(name = "sync.store.type", havingValue = "activemq")
public class ActiveMQProducer extends BaseService {

    @Value("${sync.activemq.brokerUrl}")
    private String brokerUrl;

    @Value("${sync.activemq.queueName}")
    private String queueName;

    private PooledConnectionFactory pooledConnectionFactory;

    @PostConstruct
    public void init() {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

        // 配置连接工厂
        pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(5);
        logger.info("ActiveMQ初始化生产者完成[brokerUrl:{},queueName:{}]", this.brokerUrl, this.queueName);
    }


    @PreDestroy
    public void destroy() {
        this.pooledConnectionFactory.stop();
        logger.info("ActiveMQ生产者[brokerUrl:{},queueName:{}]销毁", this.brokerUrl, this.queueName);
    }

    public String send(String textMsg) throws Exception {

        Connection connection = null;
        MessageProducer messageProducer = null;
        Session session = null;
        TextMessage textMessage;

        try {

            //1 、通过连接工厂获取连接
            connection = pooledConnectionFactory.createConnection();
            connection.start();

            //2、创建session会话
            //里面会有两个参数，第一个为事物，第二个是签收
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            //3、创建目的地（具体是队列还是主题）,这里是创建队列
            Queue queue = session.createQueue(queueName);

            //4、创建消息生产者，队列模式
            messageProducer = session.createProducer(queue);

            //5、通过messageProducer生产三条消息发送到MQ消息队列中
            textMessage =  session.createTextMessage(textMsg);

            //6、数据持久化
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //7、通过messageProducer发送给mq
            messageProducer.send(textMessage);

        } finally {

            if (messageProducer != null) {
                messageProducer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {

                connection.close();
            }
        }

        return textMessage.getJMSMessageID();
    }


}
