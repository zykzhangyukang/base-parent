package com.coderman.sync.producer;

import com.coderman.service.util.SpringContextUtil;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "队列名称")
    private String queueName;

    @ApiModelProperty(value = "broker地址")
    private String brokerUrl;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "是否启用")
    private boolean enable;

    public Message send(String msg) throws JMSException {
        JmsTemplate jmsTemplate = SpringContextUtil.getBean(JmsTemplate.class);
        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText(msg);
        jmsTemplate.convertAndSend(message);
        return message;
    }
}
