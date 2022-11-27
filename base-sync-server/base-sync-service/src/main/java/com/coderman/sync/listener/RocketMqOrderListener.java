package com.coderman.sync.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RocketMqOrderListener implements MessageListenerOrderly {

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
