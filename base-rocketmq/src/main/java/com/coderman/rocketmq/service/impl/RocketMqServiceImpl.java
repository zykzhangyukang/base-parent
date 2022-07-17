package com.coderman.rocketmq.service.impl;

import com.coderman.rocketmq.dao.MqMsgDAO;
import com.coderman.rocketmq.enums.MsgStatus;
import com.coderman.rocketmq.model.MqMsgExample;
import com.coderman.rocketmq.model.MqMsgModel;
import com.coderman.rocketmq.service.RocketMqService;
import com.coderman.service.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author coderman
 * @date 2022/6/2222:13
 */
@Service
@Slf4j
public class RocketMqServiceImpl implements RocketMqService {

    @Autowired
    private MqMsgDAO mqMsgDAO;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Value("${spring.application.name}")
    private String applicationName;


    private final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);


    @Override
    public void sendMsg(String tag, String msg) {

        final MqMsgModel msgModel = new MqMsgModel();

        msgModel.setTag(tag);
        msgModel.setRetry(0);
        msgModel.setCreateTime(new Date());
        msgModel.setUuid(UUIDUtils.getPrimaryValue());
        msgModel.setMsg(msg);

        msgModel.setSendStatus(MsgStatus.wait.name());

        this.mqMsgDAO.insert(msgModel);

        // 先判断是否有事务,如果有本地执行提交之后,在事务提交之后发送消息到RocketMQ
        if (TransactionSynchronizationManager.isActualTransactionActive()) {


            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

                @Override
                public void afterCommit() {

                    log.info("rocket send after tx,msg:{}", msg);
                    sendMsg0(msgModel);
                }
            });
        } else {

            log.info("rocket send after no tx,msg:{}", msg);
            sendMsg0(msgModel);
        }

    }

    private void sendMsg0(final MqMsgModel msg) {

        this.threadPoolExecutor.execute(() -> {

            try {

                MqMsgModel model = new MqMsgModel();

                // 先将状态改为发送中
                if (MsgStatus.fail.name().equals(msg.getSendStatus())) {
                    model.setRetry(msg.getRetry() + 1);
                }

                model.setUuid(msg.getUuid());
                model.setSendStatus(MsgStatus.sending.name());

                mqMsgDAO.updateByPrimaryKeySelective(model);

                // 发送消息,没有抛出异常说明消息发送成功
                Message message = new Message(applicationName + "_topic", msg.getTag(), StringUtils.EMPTY, msg.getMsg().getBytes(StandardCharsets.UTF_8));
                SendResult sendResult = defaultMQProducer.send(message);


                if (null != sendResult) {

                    model.setMid(sendResult.getMsgId());
                    model.setSendStatus(MsgStatus.sent.name());
                } else {

                    model.setSendStatus(MsgStatus.fail.name());
                }

                mqMsgDAO.updateByPrimaryKeySelective(model);


            } catch (Exception e) {


                MqMsgModel model = new MqMsgModel();

                model.setUuid(msg.getUuid());
                model.setSendStatus(MsgStatus.fail.name());

                mqMsgDAO.updateByPrimaryKeySelective(model);

                log.error("send rocket error:{}",e.getMessage());
            }
        });

    }

    @Override
    public void resendMsg() {

        // 将待发送/发送中，创建时间在10分钟前的消息标记为失败
        MqMsgExample example = new MqMsgExample();
        MqMsgExample.Criteria criteria = example.createCriteria();

        criteria.andSendStatusIn(Arrays.asList(MsgStatus.sending.name(), MsgStatus.wait.name()));
        criteria.andCreateTimeLessThanOrEqualTo(new Date(System.currentTimeMillis() - 10 * 60 * 1000));

        MqMsgModel model = new MqMsgModel();
        model.setSendStatus(MsgStatus.fail.name());

        this.mqMsgDAO.updateByExampleSelective(model, example);


        // 处理发送失败，重试次数在6次以内的记录
        MqMsgExample mqMsgExample = new MqMsgExample();
        MqMsgExample.Criteria exampleCriteria = mqMsgExample.createCriteria();

        exampleCriteria.andSendStatusEqualTo(MsgStatus.fail.name());
        exampleCriteria.andRetryLessThanOrEqualTo(6);

        List<MqMsgModel> mqMsgModels = this.mqMsgDAO.selectByExample(mqMsgExample);

        if (!CollectionUtils.isEmpty(mqMsgModels)) {

            StopWatch stopWatch = new StopWatch();


            stopWatch.start();
            for (MqMsgModel mqMsgModel : mqMsgModels) {

                this.sendMsg0(mqMsgModel);
            }
            stopWatch.stop();


            log.info("失败消息补偿定时器,本次处理消息size:{}, 失败消息补偿定时器,耗时:{} ms", mqMsgModels.size(), stopWatch.getTotalTimeMillis());
        }

    }
}
