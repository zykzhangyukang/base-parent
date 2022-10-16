package com.coderman.sync.listener;

import com.coderman.api.constant.CommonConstant;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.context.SyncContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RocketMqListener implements MessageListenerConcurrently {

    private final static Logger logger = LoggerFactory.getLogger(RocketMqListener.class);
    private final static String SYNC_MSG_ID = "sync_msg_id";
    private final static String SYNC_MSG_LOCK = "sync_msg_lock";

    private final static Integer SYNC_REDID_DB = 0;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        int retryTimeLimit = 8;


        for (MessageExt message : messageExtList) {

            // 获取消息
            String msg = null;

            try {


                if (message.getReconsumeTimes() > retryTimeLimit) {

                    logger.error("投送次数超过:" + retryTimeLimit + "次,不处理当前消息,当前" + message.getReconsumeTimes() + "次,msgID:" + message.getMsgId());
                    continue;
                }

                msg = new String(message.getBody(), StandardCharsets.UTF_8);

            } catch (Exception e) {

                logger.error("MQ消息解码失败", e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }


            try {

                String result = SyncContext.getContext().syncData(msg, message.getMsgId(), PlanConstant.MSG_ROCKET_MQ, message.getReconsumeTimes());

                if (!SyncContext.SYNC_END.equalsIgnoreCase(result)) {

                    logger.error("_sync_mq_listener:同步结果:" + result);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            } catch (Exception e) {

                logger.error("消息消息失败:{}", e.getMessage(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
