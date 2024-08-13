package com.coderman.sync.producer;

import com.coderman.service.util.SpringContextUtil;
import lombok.Data;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @author zhangyukang
 */
@Data
public class ActiveMQProducer {

    public Message send(String msg) throws JMSException {
        JmsTemplate jmsTemplate = SpringContextUtil.getBean(JmsTemplate.class);
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText(msg);
        jmsTemplate.convertAndSend(message);
        return message;
    }
}
