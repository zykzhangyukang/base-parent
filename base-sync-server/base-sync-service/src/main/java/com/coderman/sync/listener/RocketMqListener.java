package com.coderman.sync.listener;

import com.coderman.service.redis.RedisService;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RocketMqListener implements MessageListenerConcurrently {

    private final static Logger logger = LoggerFactory.getLogger(RocketMqListener.class);
    private final static String SYNC_MSG_ID = "sync_msg_id";
    private final static String SYNC_MSG_ID_FLAG = "1";

    private final static Integer SYNC_REDID_DB = 1;

    @Resource
    private RedisService redisService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext context) {

        int retryTimeLimit = 8;

        String redisKey = SYNC_MSG_ID + messageExtList.get(0).getMsgId();

        try {

            String syncMsgIdFlag = redisService.getString(redisKey, SYNC_REDID_DB);

            if (StringUtils.isNotBlank(syncMsgIdFlag) && SYNC_MSG_ID_FLAG.equalsIgnoreCase(syncMsgIdFlag)) {

                logger.error("consumeMessage-重复消息,标记成功:" + messageExtList.get(0).getMsgId());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        } catch (Exception e) {

            logger.error("consumeMessage-exception:" + e.getMessage());
        }


        for (MessageExt message : messageExtList) {

            // 获取消息
            String msg;

            try {

                if (message.getReconsumeTimes() > retryTimeLimit) {

                    logger.error("投送次数超过:" + retryTimeLimit + "次,不处理当前消息,当前" + message.getReconsumeTimes() + "次,msgID:" + message.getMsgId());
                    continue;
                }

                msg = new String(message.getBody(), StandardCharsets.UTF_8);

            } catch (Exception e) {

                logger.error("MQ消息解码失败:{}", e.getMessage());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }


            try {

                String result = SyncContext.getContext().syncData(msg, message.getMsgId(), PlanConstant.MSG_ROCKET_MQ, message.getReconsumeTimes());

                if (!SyncConstant.SYNC_END.equalsIgnoreCase(result)) {

                    logger.error("RocketMqListener同步结果:{}", result);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            } catch (Exception e) {

                logger.error("消息处理异常:{}", e.getMessage());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        }

        // 如果没有异常都任务消费成功
        redisService.setString(redisKey, SYNC_MSG_ID_FLAG, 60, SYNC_REDID_DB);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}

