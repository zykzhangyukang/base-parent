package com.coderman.rocketmq.jobhandler;

import com.coderman.rocketmq.service.RocketMqSqlService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 失败消息重试
 */
@JobHandler(value="resentJobHandler")
@Component
@Slf4j
public class ResentJobHandler extends IJobHandler {

    @Resource
    private RocketMqSqlService rocketMqSqlService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        rocketMqSqlService.resendMsg();

        return ReturnT.SUCCESS;
    }
}
