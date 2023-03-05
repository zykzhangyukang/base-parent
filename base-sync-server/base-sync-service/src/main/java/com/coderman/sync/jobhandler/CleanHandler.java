package com.coderman.sync.jobhandler;

import com.coderman.sync.config.MultiDatasourceConfig;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.executor.AbstractExecutor;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.SyncConvert;
import com.coderman.sync.util.SqlUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@JobHandler(value = "cleanHandler")
@Component
@Slf4j
public class CleanHandler extends IJobHandler {

    @Resource
    private MultiDatasourceConfig datasourceConfig;

    @SneakyThrows
    @Override
    public ReturnT<String> execute(String param) {

        Date ltTime = DateUtils.addMinutes(new Date(), -5);

        Set<String> databaseSets = new HashSet<>();

        List<String> messageDatabases = datasourceConfig.listDatabases("message");
        List<String> callbackDatabases = datasourceConfig.listDatabases("callback");

        databaseSets.addAll(messageDatabases);
        databaseSets.addAll(callbackDatabases);


        for (String dbname : databaseSets) {

            try {


                /***************************** 删除本地消息表冗余数据 ********************************/

                // 获取db类型
                String dbType = SyncContext.getContext().getDbType(dbname);

                // 批量参数sql
                String batchDelSql = StringUtils.EMPTY;

                List<Object> paramList = new ArrayList<>();

                if (messageDatabases.contains(dbname)) {

                    // 封装参数
                    if (SyncConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {

                        batchDelSql = "delete from pub_mq_message where deal_status='success' and create_time <? limit ?;";
                        paramList.add(ltTime);
                        paramList.add(10000);

                    } else if (SyncConstant.DB_TYPE_MSSQL.equalsIgnoreCase(dbType)) {

                        batchDelSql = "delete from pub_mq_message where mq_message_id in (select top " + 10000 + " mq_message_id from pub_mq_message where deal_status = 'success' and create_time <?)";
                        paramList.add(ltTime);
                    }

                    this.deleteLoop(dbname, batchDelSql, paramList);
                }


                /******************************** 删除回调消息表冗余数据 ********************************/

                paramList.clear();

                if (callbackDatabases.contains(dbname)) {

                    if (SyncConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {

                        batchDelSql = "delete from pub_callback where status='success' and create_time < ? limit ?;";
                        paramList.add(ltTime);
                        paramList.add(10000);

                    } else if (SyncConstant.DB_TYPE_MSSQL.equalsIgnoreCase(dbType)) {

                        batchDelSql = "delete from pub_callback where callback_id in (select top " + 10000 + " callback_id from pub_callback where status = 'success' and create_time <?)";
                        paramList.add(ltTime);
                    }

                    this.deleteLoop(dbname, batchDelSql, paramList);
                }


            } catch (Exception e) {

                XxlJobLogger.log("清除冗余数据失败:" + dbname + ",msg:" + e.getMessage());

                log.error("清除冗余数据失败:{}", e.getMessage(), e);
            }

        }


        return ReturnT.SUCCESS;
    }

    /**
     * 循环删除
     *
     * @param dbName      数据库名称
     * @param batchDelSql sql
     * @param paramList   参数列表
     * @throws Throwable
     */
    private void deleteLoop(String dbName, String batchDelSql, List<Object> paramList) throws Throwable {

        int batchNum = 1;
        int deleteRows = -1;

        while (deleteRows != 0) {

            AbstractExecutor executor = AbstractExecutor.build(dbName);

            SqlMeta sqlMeta = new SqlMeta();
            sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_DELETE);
            sqlMeta.setSql(batchDelSql);
            sqlMeta.setParamList(SyncConvert.toArrayList(paramList));


            executor.sql(sqlMeta);

            sqlMeta.setSql(SqlUtil.fillParam(sqlMeta,executor));

            List<SqlMeta> resultList = executor.execute();

            if (CollectionUtils.isNotEmpty(resultList) && null != resultList.get(0)) {

                deleteRows = resultList.get(0).getAffectNum();

            } else {

                deleteRows = 0;
            }

            XxlJobLogger.log("清除冗余数据消息记录,第" + batchNum + "批, 数据库->" + dbName + " 条数->" + deleteRows);

            log.info("清除冗余数据消息记录:{},dbName:{}", deleteRows, dbName);

            batchNum++;
        }

    }
}
