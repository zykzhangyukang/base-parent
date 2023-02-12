package com.coderman.sync.jobhandler;

import com.coderman.sync.context.SyncContext;
import com.coderman.sync.result.ResultModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@JobHandler(value = "resultHandler")
@Component
@Slf4j
public class ResultHandler extends IJobHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public ReturnT<String> execute(String param) {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("刷新同步记录到ES");

        Date now = new Date();

        Date endTime = DateUtils.addMinutes(now, -5);
        Date startTime = DateUtils.addMinutes(now, -20);

        final String sql = "select uuid,plan_uuid,plan_code,plan_name,msg_src" +
                ",mq_id,msg_id,msg_content,src_project,dest_project,sync_content,msg_create_time,sync_time" +
                ",status,error_msg,repeat_count,remark,sync_to_es from pub_sync_result where sync_to_es = ? and msg_create_time > ? and msg_create_time < ?";

        List<ResultModel> resultModels = this.jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ResultModel.class), 0, startTime, endTime);

        if (CollectionUtils.isNotEmpty(resultModels)) {

            for (ResultModel resultModel : resultModels) {

                SyncContext.getContext().syncToEs(resultModel);
            }

        }

        stopWatch.stop();

        XxlJobLogger.log(stopWatch.prettyPrint());
        XxlJobLogger.log("总记录数:" + resultModels.size());

        log.info("刷新记录到ES总数:{}",resultModels.size());

        return ReturnT.SUCCESS;
    }
}
