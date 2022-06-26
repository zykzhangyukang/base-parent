package com.coderman.rocketmq.timer;

import com.coderman.rocketmq.dao.MqMsgDAO;
import com.coderman.rocketmq.enums.MsgStatus;
import com.coderman.rocketmq.model.MqMsgExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author coderman
 * @Title: 历史消息清除定时任务
 * @date 2022/6/2515:18
 */
@Configuration
@EnableScheduling
@SuppressWarnings("all")
@Slf4j
public class MsgHistoryClearTimer {

    @Autowired
    private MqMsgDAO mqMsgDAO;


    /**
     * 每天凌晨删除过期的数据
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void clearHistoryMsgData() {

        MqMsgExample example = new MqMsgExample();
        example.createCriteria().andSendStatusEqualTo(MsgStatus.sent.name());
        int rowCount = this.mqMsgDAO.deleteByExample(example);

        if(rowCount>0){
            log.info("历史消息清除定时任务,size:{}",rowCount);
        }
    }
}
