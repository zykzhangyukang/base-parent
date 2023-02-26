package com.coderman.sync.task.support;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.executor.AbstractExecutor;
import com.coderman.sync.plan.meta.ColumnMeta;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.plan.meta.TableMeta;
import com.coderman.sync.sql.SelectBuilder;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.SyncConvert;
import com.coderman.sync.task.SyncTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetDataTask extends AbstractTask {


    private boolean onlyDelete;


    public GetDataTask(SyncTask syncTask) {
        super.setSyncTask(syncTask);
    }


    /**
     * 构建查询源表任务
     *
     * @param syncTask 同步任务
     * @return
     * @throws SyncException
     */
    public static GetDataTask build(SyncTask syncTask) throws SyncException {

        GetDataTask getDataTask = new GetDataTask(syncTask);
        getDataTask.createExecutor();

        return getDataTask;
    }

    /**
     * 创建执行器
     *
     * @throws SyncException
     */
    private void createExecutor() throws SyncException {

        SyncTask syncTask = super.getSyncTask();

        String srcDb = syncTask.getPlanMeta().getDbMeta().getSrcDb();
        String dbType = SyncContext.getContext().getDbType(srcDb);


        // 构建执行器
        AbstractExecutor executor = AbstractExecutor.build(srcDb);

        for (MsgTableMeta msgTableMeta : syncTask.getMsgMeta().getTableMetaList()) {


            TableMeta tableMeta = SyncContext.getContext().getTableMeta(syncTask.getPlanMeta().getCode(), msgTableMeta.getCode());


            if (SyncConstant.OPERATE_TYPE_DELETE.equals(tableMeta.getType())) {

                this.onlyDelete = true;
                continue;
            }

            // 构建查询语句
            SelectBuilder selectBuilder = SelectBuilder.create(dbType);
            selectBuilder.table(tableMeta.getSrc());

            for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {
                selectBuilder.column(columnMeta.getSrc());
            }

            // where 列也要查询处理,便于后面封住结果集
            selectBuilder.column(tableMeta.getUnique().getKey());
            selectBuilder.whereIn(tableMeta.getUnique().getKey(), msgTableMeta.getUniqueList().size());

            String uniqueType = msgTableMeta.getUniqueType();

            List<Object> paramList = SyncConvert.convert(msgTableMeta.getUniqueList(), uniqueType);

            // 封装对象和参数集合
            SqlMeta sqlMeta = new SqlMeta();
            sqlMeta.setSql(selectBuilder.sql());
            sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_SELECT);
            sqlMeta.setUniqueKey(tableMeta.getUnique().getKey());
            sqlMeta.setParamList(SyncConvert.toArrayList(paramList));
            sqlMeta.setTableCode(tableMeta.getCode());

            executor.sql(sqlMeta);
        }

        if (CollectionUtils.isNotEmpty(executor.getSqlList()) && this.onlyDelete) {

            this.onlyDelete = false;
        }

        super.setExecutor(executor);
    }


    public boolean isOnlyDelete() {
        return onlyDelete;
    }

    public void setOnlyDelete(boolean onlyDelete) {
        this.onlyDelete = onlyDelete;
    }
}
