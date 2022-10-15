package com.coderman.rocketmq.timer;

import com.coderman.rocketmq.service.RocketMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author coderman
 * @Description: MQ消息补偿器
 * @date 2022/6/2514:43
 */
@Configuration
@EnableScheduling
@SuppressWarnings("all")
public class MsgCompensationTimer {

    @Autowired
    private RocketMqService rocketMqService;


    @Scheduled(cron = "*/5 * * * * ?")
    private void compensationTask() {

        // todo use xxljob
        //this.rocketMqService.resendMsg();
    }
}
