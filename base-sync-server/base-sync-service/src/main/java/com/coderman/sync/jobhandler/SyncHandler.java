package com.coderman.sync.jobhandler;

import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.config.MultiDatasourceConfig;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.task.base.MsgTask;
import com.coderman.sync.util.SqlUtil;
import com.google.common.collect.Lists;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JobHandler(value = "syncHandler")
@Component
@Slf4j
public class SyncHandler extends IJobHandler {

    @Resource
    private MultiDatasourceConfig multiDatasourceConfig;

    @Override
    public ReturnT<String> execute(String param) {


        List<String> databases = multiDatasourceConfig.listDatabases("message");

        for (String dbName : databases) {

            JdbcTemplate jdbcTemplate = SpringContextUtil.getBean(dbName + "_template");

            String sql = "select mq_message_id from pub_mq_message where send_status in ('sending','wait') and create_time < ?";

            // 1. 查询5分钟前处于发送中或者待发送的记录
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, DateUtils.addMinutes(new Date(), -5));

            StringBuilder sb = new StringBuilder();

            sb.append("处理MQ消息发送失败记录.").append(dbName);

            if (CollectionUtils.isNotEmpty(resultList)) {

                List<Object> idList = resultList.stream().map(e -> e.get("mq_message_id")).collect(Collectors.toList());

                sb.append("预查询->").append(idList.size());

                // 2. 5分钟前处于发送中或者待发送的记录标记为失败
                sql = "update pub_mq_message set send_status = 'fail' where send_status in ('sending','wait') and mq_message_id in ";

                List<List<Object>> updateList = Lists.partition(idList, 50);

                List<String> updateSqlList = updateList.stream().map(tmp -> StringUtils.join(tmp, ",")).collect(Collectors.toList());

                int updateCount = 0;

                for (String tempSql : updateSqlList) {

                    tempSql = sql + "( select " + tempSql.replaceAll(",", " union select ") + ")";
                    tempSql += ";";

                    int rowCount = jdbcTemplate.update(tempSql);

                    updateCount += rowCount;
                }

                sb.append("实际更新->").append(updateCount);

            }

            // 3. 查询需处理的消息内容
            sql = "select msg_content from pub_mq_message where deal_count < ? and create_time > ? and create_time < ? and send_status = 'fail' and deal_status in ('wait','fail')";

            int retry = 5;
            Date startTime = DateUtils.addMinutes(new Date(), -20);
            Date endTime = DateUtils.addMinutes(new Date(), -5);

            List<Map<String, Object>> resList = jdbcTemplate.queryForList(sql, retry, startTime, endTime);

            sb.append("需处理->").append(resList.size());

            if (CollectionUtils.isNotEmpty(resList)) {

                for (Map<String, Object> resultMap : resList) {

                    MsgTask msgTask = new MsgTask();

                    if (resultMap.containsKey("msg_content")) {

                        msgTask.setMsg(resultMap.get("msg_content").toString());

                    } else {

                        msgTask.setMsg(SqlUtil.getFieldName("msg_content"));
                    }

                    msgTask.setSource(PlanConstant.MSG_SOURCE_JOB);
                    SyncContext.getContext().addTaskToDelayQueue(msgTask);

                }

            }

            XxlJobLogger.log(sb.toString());
            log.info(sb.toString());
        }

        return ReturnT.SUCCESS;
    }
}
