package com.coderman.sync.task;

import com.coderman.sync.plan.meta.MsgMeta;
import com.coderman.sync.plan.meta.PlanMeta;
import com.coderman.sync.result.ResultModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
    public static SyncTask build(String msg, String mqId, int retryTimes) {

        SyncTask syncTask = new SyncTask();

        // 解析构建消息对象
        MsgMeta msgMeta = MsgMeta.build(msg);

        // 构建同步结果记录
        ResultModel resultModel = new ResultModel();


        return null;
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
