package com.coderman.sync.listener;

import com.coderman.service.redis.RedisService;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RocketMqOrderListener implements MessageListenerOrderly {

    private final static Logger logger = LoggerFactory.getLogger(RocketMqListener.class);
    private final static String SYNC_ORDER_MSG_ID = "sync_msg_id";
    private final static String SYNC_ORDER_MSG_ID_FLAG = "1";

    private final static Integer SYNC_ORDER_REDID_DB = 2;

    @Resource
    private RedisService redisService;

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExtList, ConsumeOrderlyContext context) {

        int retryTimeLimit = 8;

        String redisKey = SYNC_ORDER_MSG_ID + messageExtList.get(0).getMsgId();

        try {

            String syncMsgIdFlag = redisService.getString(redisKey, SYNC_ORDER_REDID_DB);

            if (StringUtils.isNotBlank(syncMsgIdFlag) && SYNC_ORDER_MSG_ID_FLAG.equalsIgnoreCase(syncMsgIdFlag)) {

                logger.error("consumeOrderMessage-重复消息,标记成功:" + messageExtList.get(0).getMsgId());
                return ConsumeOrderlyStatus.SUCCESS;
            }

        } catch (Exception e) {

            logger.error("consumeOrderMessage-exception:" + e.getMessage());
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
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }


            try {

                String result = SyncContext.getContext().syncData(msg, message.getMsgId(), PlanConstant.MSG_ROCKET_MQ, message.getReconsumeTimes());

                if (!SyncConstant.SYNC_END.equalsIgnoreCase(result)) {

                    logger.error("RocketMqOrderListener同步结果:{}", result);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

            } catch (Exception e) {

                logger.error("消息处理异常:{}", e.getMessage());
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }

        }

        // 如果没有异常都任务消费成功
        redisService.setString(redisKey, SYNC_ORDER_MSG_ID_FLAG, 60, SYNC_ORDER_REDID_DB);

        return ConsumeOrderlyStatus.SUCCESS;
    }
}
