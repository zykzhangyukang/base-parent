package com.coderman.sync.task;

import com.coderman.service.util.UUIDUtils;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.plan.meta.MsgMeta;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.result.ResultModel;
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

    /**
     * 构建同步任务
     *
     * @param msg
     * @param mqId
     * @param retryTimes
     * @return
     */
    public static SyncTask build(String msg, String mqId,String msgSrc, int retryTimes) {

        SyncTask syncTask = new SyncTask();

        // 解析构建消息对象
        MsgMeta msgMeta = MsgMeta.build(msg);

        // 构建同步结果记录
        ResultModel resultModel = new ResultModel();
        resultModel.setUuid(UUIDUtils.getPrimaryValue());
        resultModel.setStatus(PlanConstant.RESULT_STATUS_SUCCESS);
        resultModel.setMsgCreateTime(msgMeta.getCreateDate());
        resultModel.setMsgContent(msg);
        resultModel.setMsgId(mqId);
        resultModel.setMqId(mqId);
        resultModel.setRepeatCount(retryTimes);
        resultModel.setSyncTime(new Date());
        resultModel.setPlanSrc(msgSrc);
        resultModel.setErrorMsg(StringUtils.EMPTY);

        PlanMeta planMeta = SyncContext.getContext().getPlanMeta(msgMeta.getPlanCode());

        if(planMeta == null){

            log.error("同步计划不存在,{}",msg);
            resultModel.setErrorMsg("同步计划不存在,"+msg);
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

            if(!planMeta.containsCode(tableMeta.getCode())){

                resultModel.setErrorMsg("同步计划"+planMeta.getCode()+"中,不存在"+tableMeta.getCode()+","+msg);
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
     * @return
     */
    public String sync() {
        return null;
    }
}
