package com.coderman.sync.task;

import com.alibaba.fastjson.JSON;
import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.plan.meta.MsgMeta;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.task.support.GetDataTask;
import com.coderman.sync.task.support.SyncDataTask;
import com.coderman.sync.task.support.WriteBackTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 同步任务
 */
@Slf4j
@Data
public class SyncTask {

    // 同步计划对象
    private PlanMeta planMeta;

    // 封装消息对象
    private MsgMeta msgMeta;

    // 封装同步结果
    private ResultModel resultModel;

    // 同步时间
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
        resultModel.setStatus(PlanConstant.RESULT_STATUS_SUCCESS);
        resultModel.setMsgCreateTime(msgMeta.getCreateDate());
        resultModel.setMsgContent(msg);
        resultModel.setMsgId(msgMeta.getMsgId());
        resultModel.setMqId(mqId);
        resultModel.setRepeatCount(retryTimes);
        resultModel.setSyncTime(new Date());
        resultModel.setPlanSrc(msgSrc);
        resultModel.setErrorMsg(StringUtils.EMPTY);

        PlanMeta planMeta = SyncContext.getContext().getPlanMeta(msgMeta.getPlanCode());

        if (planMeta == null) {

            log.error("同步计划不存在,{}", msg);
            resultModel.setErrorMsg("同步计划不存在," + msg);
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
    public String sync() {

        TaskResult taskResult = new TaskResult();


        // 判断同步任务是否正常
        if (StringUtils.isNotBlank(this.resultModel.getErrorMsg())) {

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
                return taskResult.isRetry() ? SyncConstant.SYNC_RETRY : SyncConstant.SYNC_END;
            }


            // 2. 同步数据
            SyncDataTask syncDataTask = SyncDataTask.build(this, taskResult);
            taskResult = syncDataTask.process();

            if (SyncConstant.TASK_CODE_FAIL.equalsIgnoreCase(taskResult.getCode())) {

                this.resultModel.setErrorMsg(this.resultModel.getErrorMsg() + taskResult.getErrorMsg());
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

            return taskResult.isRetry() ? SyncConstant.SYNC_RETRY : SyncConstant.SYNC_END;

        } catch (Throwable e) {

            log.error("同步数据出错,msgContent->" + this.resultModel.getMsgContent() + ",exception->" + e);
            this.resultModel.setErrorMsg(e.getMessage());
        }

        return SyncConstant.SYNC_RETRY;


    }

}
