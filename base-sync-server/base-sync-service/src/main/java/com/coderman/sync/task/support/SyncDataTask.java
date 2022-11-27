package com.coderman.sync.task.support;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.executor.AbstractExecutor;
import com.coderman.sync.plan.meta.ColumnMeta;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.plan.meta.TableMeta;
import com.coderman.sync.sql.DeleteBuilder;
import com.coderman.sync.sql.InsertBuilder;
import com.coderman.sync.sql.UpdateBuilder;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.SyncConvert;
import com.coderman.sync.task.SyncTask;
import com.coderman.sync.task.TaskResult;
import org.springframework.jdbc.support.JdbcUtils;

import java.util.*;

/**
 * 同步数据任务
 */
public class SyncDataTask extends AbstractTask {


    private SyncDataTask(SyncTask syncTask) {

        super.setSyncTask(syncTask);
    }

    public SyncDataTask() {

    }

    public static SyncDataTask build(SyncTask syncTask, TaskResult taskResult) {

        SyncDataTask syncDataTask = new SyncDataTask(syncTask);

        syncDataTask.createExecutor(taskResult);
        return syncDataTask;
    }

    @SuppressWarnings("all")
    private void createExecutor(TaskResult taskResult) throws SyncException {


        SyncTask syncTask = super.getSyncTask();
        String destDb = syncTask.getPlanMeta().getDbMeta().getDestDb();
        String dbType = SyncContext.getContext().getDbType(destDb);

        // 构建执行器
        AbstractExecutor executor = AbstractExecutor.build(destDb);
        super.setExecutor(executor);


        Map<String, SqlMeta> resultMetaMap = new HashMap<>();
        if (null != taskResult && null != taskResult.getResultList()) {

            for (SqlMeta sqlMeta : taskResult.getResultList()) {

                resultMetaMap.put(sqlMeta.getTableCode(), sqlMeta);
            }
        }

        for (MsgTableMeta msgTableMeta : syncTask.getMsgMeta().getTableMetaList()) {


            TableMeta tableMeta = SyncContext.getContext().getTableMeta(syncTask.getPlanMeta().getCode(), msgTableMeta.getCode());

            // 封装对象和参数集合
            SqlMeta sqlMeta = new SqlMeta();
            sqlMeta.setTableCode(tableMeta.getCode());

            if (SyncConstant.OPERATE_TYPE_INSERT.equals(tableMeta.getType())) {


                // 插入操作
                InsertBuilder insertBuilder = InsertBuilder.create(dbType);
                insertBuilder.table(tableMeta.getDest());

                int[] paramTypeList = new int[tableMeta.getColumnMetas().size()];

                int i = 0;

                for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {

                    insertBuilder.column(columnMeta.getDest());
                    paramTypeList[i] = JdbcUtils.TYPE_UNKNOWN;
                    i++;
                }

                insertBuilder.column("sync_time");
                List<Object[]> paramList = this.generateParam(resultMetaMap, msgTableMeta, tableMeta, syncTask.getSyncTime(), true);

                insertBuilder.groupCount(paramList.size());
                sqlMeta.setParamList(paramList);
                sqlMeta.setSql(insertBuilder.sql());
                sqlMeta.setArgTypes(paramTypeList);
                sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_INSERT);
            }

            if (SyncConstant.OPERATE_TYPE_DELETE.equals(tableMeta.getType())) {

                DeleteBuilder deleteBuilder = DeleteBuilder.create(dbType);
                deleteBuilder.table(tableMeta.getDest());
                deleteBuilder.whereIn(tableMeta.getUnique().getValue(), msgTableMeta.getUniqueList().size());
                deleteBuilder.groupCount(1);

                sqlMeta.setSql(deleteBuilder.sql());
                sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_DELETE);

                String targetType = msgTableMeta.getUniqueType();

                if ("_id".equals(tableMeta.getUnique().getValue())) {
                    targetType = SyncConvert.DATA_TYPE_OBJECTID;
                }

                sqlMeta.setParamList(SyncConvert.toArrayList(SyncConvert.convert(msgTableMeta.getUniqueList(), targetType)));
            }

            if (SyncConstant.OPERATE_TYPE_UPDATE.equals(tableMeta.getType())) {

                UpdateBuilder updateBuilder = UpdateBuilder.create(dbType);
                updateBuilder.table(tableMeta.getDest());

                int[] paramTypeList = new int[tableMeta.getColumnMetas().size()];
                int i = 0;

                for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {

                    updateBuilder.column(columnMeta.getDest());
                    paramTypeList[i] = JdbcUtils.TYPE_UNKNOWN;
                    i++;
                }

                updateBuilder.column("sync_time");
                updateBuilder.whereIn(tableMeta.getUnique().getValue(), 1);

                List<Object[]> paramList = this.generateParam(resultMetaMap, msgTableMeta, tableMeta, syncTask.getSyncTime(), false);
                updateBuilder.groupCount(paramList.size());

                sqlMeta.setParamList(paramList);
                sqlMeta.setSql(updateBuilder.sql());
                sqlMeta.setArgTypes(paramTypeList);
                sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_UPDATE);
            }

            executor.sql(sqlMeta);
        }

    }

    private List<Object[]> generateParam(Map<String, SqlMeta> resultMetaMap, MsgTableMeta msgTableMeta, TableMeta tableMeta, Date syncTime, boolean insert) {

        SqlMeta resultMeta = resultMetaMap.get(msgTableMeta.getCode());

        List<Object[]> paramList = new ArrayList<>();

        // 封装参数,与同步计划保存一致的顺序
        for (Map<String, Object> resultMap : resultMeta.getResultList()) {

            List<Object> paramObj = new ArrayList<>();

            for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {

                paramObj.add(SyncConvert.convert(resultMap.get(columnMeta.getSrc()), columnMeta.getType()));
            }

            // 同步时间
            paramObj.add(syncTime);


            // where 条件
            if (!insert) {

                paramObj.add(resultMap.get(resultMeta.getUniqueKey()));
            }

            paramList.add(paramObj.toArray());

        }


        return paramList;
    }

}
