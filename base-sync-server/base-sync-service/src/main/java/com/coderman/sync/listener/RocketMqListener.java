package com.coderman.sync.listener;

import com.coderman.service.redis.RedisService;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.exception.SyncException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RocketMQMessageListener(consumerGroup = "SYNC_CONSUMER_GROUP", topic = "SyncTopic")
@Component
public class RocketMqListener implements RocketMQListener<MessageExt> {

    private final static Logger logger = LoggerFactory.getLogger(RocketMqListener.class);
    private final static String SYNC_MSG_ID = "sync_msg_id";
    private final static String SYNC_MSG_ID_FLAG = "1";

    private final static Integer SYNC_REDID_DB = 1;

    @Resource
    private RedisService redisService;


    @Override
    public void onMessage(MessageExt message) {

        int retryTimeLimit = 2;

        String redisKey = SYNC_MSG_ID + message.getMsgId();

        try {

            String syncMsgIdFlag = redisService.getString(redisKey, SYNC_REDID_DB);
            if (StringUtils.isNotBlank(syncMsgIdFlag) && SYNC_MSG_ID_FLAG.equalsIgnoreCase(syncMsgIdFlag)) {

                logger.error("consumeMessage-重复消息,标记成功:" + message.getMsgId());
                return;
            }
        } catch (Exception e) {
            logger.error("consumeMessage-exception:" + e.getMessage());
        }


        // 获取消息
        String msg;

        try {

            if (message.getReconsumeTimes() > retryTimeLimit) {

                logger.error("投送次数超过:" + retryTimeLimit + "次,不处理当前消息,当前" + message.getReconsumeTimes() + "次,msgID:" + message.getMsgId());
                return;
            }

            msg = new String(message.getBody(), StandardCharsets.UTF_8);

        } catch (Exception e) {

            logger.error("MQ消息解码失败:{}", e.getMessage());
            throw new SyncException("MQ消息解码失败:" + e.getMessage());
        }


        try {

            String result = SyncContext.getContext().syncData(msg, message.getMsgId(), PlanConstant.MSG_ROCKET_MQ, message.getReconsumeTimes());

            if (!SyncConstant.SYNC_END.equalsIgnoreCase(result)) {

                logger.error("_sync_mq_listener:同步结果:" + result);
                throw new SyncException("消息处理失败:" + result);
            }

        } catch (Exception e) {

            logger.error("消息处理失败:{}", e.getMessage());
            throw e;
        }


        // 如果没有异常都任务消费成功
        redisService.setString(redisKey, SYNC_MSG_ID_FLAG, 60, SYNC_REDID_DB);
    }
}
