package com.coderman.sync.task.support;

import com.coderman.sync.executor.AbstractExecutor;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.SyncTask;
import com.coderman.sync.task.TaskResult;
import com.coderman.sync.task.base.BaseTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class AbstractTask extends BaseTask {


    private SyncTask syncTask;


    private AbstractExecutor executor;



    public TaskResult process() {


        List<SqlMeta> sqlMetaList = null;
        Throwable ex = null;

        try {

            sqlMetaList = this.executor.execute();

        }catch (Throwable throwable){

            ex = throwable;
            log.error("执行同步SQL失败:"+syncTask.getPlanMeta().getCode());
        }


        return TaskUtil.validateResult(sqlMetaList,this.syncTask.getMsgMeta(),ex);
    }

    public SyncTask getSyncTask() {
        return syncTask;
    }

    public void setSyncTask(SyncTask syncTask) {
        this.syncTask = syncTask;
    }

    public AbstractExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(AbstractExecutor executor) {
        this.executor = executor;
    }

    public static Logger getLog() {
        return log;
    }
}
