package com.coderman.sync.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@JobHandler(value="resultModelHandler")
@Component
@Slf4j
public class ResultModelHandler extends IJobHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        log.error("定时器执行......");

        return ReturnT.SUCCESS;
    }
}
