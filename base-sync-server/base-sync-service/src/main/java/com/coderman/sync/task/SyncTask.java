package com.coderman.sync.task;

import com.coderman.service.util.SpringContextUtil;
import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.es.EsService;
import com.coderman.sync.plan.meta.MsgMeta;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.task.support.GetDataTask;
import com.coderman.sync.task.support.SyncDataTask;
import com.coderman.sync.task.support.WriteBackTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * 同步任务
 */
@Slf4j
@Data
public class SyncTask {

    @ApiModelProperty(value = "同步计划对象")
    private PlanMeta planMeta;

    @ApiModelProperty(value = "封装消息对象")
    private MsgMeta msgMeta;

    @ApiModelProperty(value = "封装同步结果")
    private ResultModel resultModel;

    @ApiModelProperty(value = "同步时间")
    private Date syncTime;

    public SyncTask() {
        this.syncTime = new Date();
    }

    /**
     * 构建同步任务
     *
     * @param msg        消息内容
     * @param mqId       mq消息id
     * @param retryTimes 重试次数
     * @return 同步任务
     */
    public static SyncTask build(String msg, String mqId, String msgSrc, int retryTimes) {

        SyncTask syncTask = new SyncTask();

        // 解析构建消息对象
        MsgMeta msgMeta = MsgMeta.build(msg);

        // 构建同步结果记录
        ResultModel resultModel = new ResultModel();
        resultModel.setUuid(UUIDUtils.getPrimaryValue());
        resultModel.setStatus(PlanConstant.RESULT_STATUS_FAIL);
        resultModel.setMsgCreateTime(msgMeta.getCreateDate());
        resultModel.setMsgContent(msg);
        resultModel.setMsgId(msgMeta.getMsgId());
        resultModel.setMqId(mqId);
        resultModel.setRepeatCount(retryTimes);
        resultModel.setSyncTime(new Date());
        resultModel.setMsgSrc(msgSrc);
        resultModel.setErrorMsg(StringUtils.EMPTY);

        PlanMeta planMeta = SyncContext.getContext().getPlanMeta(msgMeta.getPlanCode());

        if (planMeta == null) {

            log.error("同步计划不存在,{}", msg);
            resultModel.setErrorMsg("同步计划不存在," + msg);
            syncTask.setResultModel(resultModel);
            return syncTask;
        }


        // 这里判断一下该同步消息是否已经被处理成功过.
        String sql = "select count(1) as c from pub_sync_result where msg_id=? and msg_create_time < ? and status=? and msg_src = ?";

        int count = Optional.ofNullable(SpringContextUtil.getBean(JdbcTemplate.class)
                .queryForObject(sql, Integer.class, resultModel.getMsgId(), DateUtils.addDays(new Date(), -7),
                        PlanConstant.RESULT_STATUS_SUCCESS, msgSrc)).orElse(0);

        if (count > 0) {

            log.error("该同步任务已处理,msgId=" + resultModel.getMsgId());
            resultModel.setErrorMsg("该同步任务已处理,msgId=" + resultModel.getMsgId());
            syncTask.setResultModel(resultModel);
            return syncTask;
        }


        // 完善同步结果记录
        resultModel.setPlanCode(planMeta.getCode());
        resultModel.setPlanName(planMeta.getName());
        resultModel.setPlanUuid(planMeta.getUuid());
        resultModel.setSrcProject(planMeta.getProjectMeta().getSrcProject());
        resultModel.setDestProject(planMeta.getProjectMeta().getDestProject());


        // 检查所有同步操作是否都存在
        for (MsgTableMeta tableMeta : msgMeta.getTableMetaList()) {

            if (!planMeta.containsCode(tableMeta.getCode())) {

                log.error("同步计划" + planMeta.getCode() + "中,不存在" + tableMeta.getCode() + "," + msg);
                resultModel.setErrorMsg(("同步计划" + planMeta.getCode() + "中,不存在" + tableMeta.getCode() + "," + msg));
                syncTask.setResultModel(resultModel);
                return syncTask;
            }
        }

        syncTask.setMsgMeta(msgMeta);
        syncTask.setPlanMeta(planMeta);
        syncTask.setResultModel(resultModel);
        return syncTask;
    }


    /**
     * 同步数据
     *
     * @return 同步状态
     */
    public String sync() throws IOException {

        TaskResult taskResult = new TaskResult();


        // 判断同步任务是否正常
        if (StringUtils.isNotBlank(this.resultModel.getErrorMsg())) {

            if (StringUtils.contains(this.resultModel.getErrorMsg(), "该同步任务已处理")) {

                return SyncConstant.SYNC_END;
            }

            // 插入同步记录
            this.insertRecord();

            return SyncConstant.SYNC_RETRY;
        }

        try {


            // 1. 从源表查询数据
            GetDataTask getDataTask = GetDataTask.build(this);

            if (getDataTask.isOnlyDelete()) {

                taskResult.setCode(SyncConstant.TASK_CODE_SUCCESS);

            } else {

                taskResult = getDataTask.process();
            }


            if (SyncConstant.TASK_CODE_FAIL.equalsIgnoreCase(taskResult.getCode())) {

                this.resultModel.setErrorMsg(this.resultModel.getErrorMsg() + taskResult.getErrorMsg());

                // 插入同步记录
                this.insertRecord();

                return taskResult.isRetry() ? SyncConstant.SYNC_RETRY : SyncConstant.SYNC_END;
            }


            // 2. 同步数据
            SyncDataTask syncDataTask = SyncDataTask.build(this, taskResult);
            taskResult = syncDataTask.process();

            if (SyncConstant.TASK_CODE_FAIL.equalsIgnoreCase(taskResult.getCode())) {

                this.resultModel.setErrorMsg(this.resultModel.getErrorMsg() + taskResult.getErrorMsg());

                // 插入同步记录
                this.insertRecord();

                // 主键重复,需要走数据校验
                if (!taskResult.isRetry() && StringUtils.isNotBlank(taskResult.getErrorMsg()) && taskResult.getErrorMsg().contains("主键重复")) {

                    SyncContext.getContext().addTaskToDelayQueue(syncDataTask);
                }

                return taskResult.isRetry() ? SyncConstant.SYNC_RETRY : SyncConstant.SYNC_END;
            }

            // 同步完数据,就认为处理成功
            this.resultModel.setStatus(PlanConstant.RESULT_STATUS_SUCCESS);


            // 3. 回写状态
            WriteBackTask writeBackTask = WriteBackTask.build(this);
            taskResult = writeBackTask.process();


            // 失败不影响流程,可以向下走
            if (SyncConstant.TASK_CODE_FAIL.equalsIgnoreCase(taskResult.getCode())) {

                this.resultModel.setErrorMsg(this.resultModel.getErrorMsg() + taskResult.getErrorMsg());
                SyncContext.getContext().addTaskToDelayQueue(writeBackTask);
            }

            // 插入同步记录
            this.insertRecord();

            return taskResult.isRetry() ? SyncConstant.SYNC_RETRY : SyncConstant.SYNC_END;

        } catch (Throwable e) {

            log.error("同步数据出错,msgContent->" + this.resultModel.getMsgContent() + "\n,exception->" + e,e);
            this.resultModel.setErrorMsg(e.getMessage());

            // 插入同步记录
            this.insertRecord();
        }

        return SyncConstant.SYNC_RETRY;


    }

    private void insertRecord() throws IOException {

        JdbcTemplate jdbcTemplate = SpringContextUtil.getBean(JdbcTemplate.class);

        jdbcTemplate.update(
                "insert into " +
                        "pub_sync_result(uuid,plan_uuid,plan_code,plan_name,msg_src" +
                        ",mq_id,msg_id,msg_content,src_project,dest_project,sync_content," +
                        "msg_create_time,sync_time,status,error_msg,repeat_count,remark,sync_to_es)" +
                        " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",

                preparedStatement -> {
                    preparedStatement.setString(1, resultModel.getUuid());
                    preparedStatement.setString(2, resultModel.getPlanUuid());
                    preparedStatement.setString(3, resultModel.getPlanCode());
                    preparedStatement.setString(4, resultModel.getPlanName());
                    preparedStatement.setString(5, resultModel.getMsgSrc());
                    preparedStatement.setString(6, resultModel.getMqId());
                    preparedStatement.setString(7, resultModel.getMsgId());
                    preparedStatement.setString(8, resultModel.getMsgContent());
                    preparedStatement.setString(9, resultModel.getSrcProject());
                    preparedStatement.setString(10, resultModel.getDestProject());
                    preparedStatement.setString(11, resultModel.getSyncContent());
                    preparedStatement.setObject(12, resultModel.getMsgCreateTime());
                    preparedStatement.setObject(13, resultModel.getSyncTime());
                    preparedStatement.setString(14, resultModel.getStatus());
                    preparedStatement.setString(15, resultModel.getErrorMsg());
                    preparedStatement.setInt(16, resultModel.getRepeatCount());
                    preparedStatement.setString(17, resultModel.getRemark());
                    preparedStatement.setBoolean(18, resultModel.isSyncToEs());
                });

        // 同步到ES
        SyncContext.getContext().syncToEs(resultModel);

        // 重试成功需要把之前失败的消息标记为成功
        boolean condition1 = PlanConstant.RESULT_STATUS_SUCCESS.equals(resultModel.getStatus()) && null != resultModel.getRepeatCount() && resultModel.getRepeatCount() > 0;
        boolean condition2 = PlanConstant.RESULT_STATUS_SUCCESS.equals(resultModel.getStatus()) && StringUtils.equals(resultModel.getMsgSrc(), PlanConstant.MSG_SOURCE_JOB);

        if (condition1 || condition2) {

            String remark = "系统标记成功";

            jdbcTemplate.update("update pub_sync_result set status=?,remark=? where msg_id=? and status=?", preparedStatement -> {

                preparedStatement.setString(1, PlanConstant.RESULT_STATUS_SUCCESS);
                preparedStatement.setString(2, remark);
                preparedStatement.setString(3, this.resultModel.getMsgId());
                preparedStatement.setString(4, PlanConstant.RESULT_STATUS_FAIL);
            });

            // 标记ES状态
            SpringContextUtil.getBean(EsService.class).updateSyncResultSuccess(this.resultModel, remark);
        }

    }

}
