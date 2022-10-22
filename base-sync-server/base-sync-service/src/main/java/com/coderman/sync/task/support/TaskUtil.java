package com.coderman.sync.task.support;

import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.plan.meta.MsgMeta;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.TransactionTimedOutException;

import java.lang.annotation.ElementType;
import java.util.*;

@Slf4j
public class TaskUtil {

    protected static TaskResult validateResult(List<SqlMeta> sqlMetaList, MsgMeta msgMeta,Throwable throwable){

        TaskResult taskResult = new TaskResult();
        taskResult.setResultList(sqlMetaList);

        if(null !=throwable){

            taskResult.setCode(SyncConstant.TASK_CODE_FAIL);
            taskResult.setRetry(true);

            if(throwable instanceof SyncException){


                SyncException ex = (SyncException) throwable;

                switch (ex.getErrorCodeEnum()){

                    case DB_NOT_CONNECT:
                        taskResult.setErrorMsg("配置错误,无法获取数据库链接,"+ex.getMessage());
                        break;
                    default:
                        taskResult.setErrorMsg("未知异常,"+ex.getMessage());
                }

                log.error("同步系统异常:{}",throwable.getMessage());


            }else if(throwable instanceof CannotGetJdbcConnectionException){

                taskResult.setErrorMsg("无法获取数据库连接,"+throwable.getMessage());
            }else if(throwable instanceof TransactionTimedOutException){

                taskResult.setErrorMsg("事务超时:"+throwable.getMessage());
            }else if(throwable instanceof DuplicateKeyException){

                taskResult.setErrorMsg("主键重复,"+throwable.getMessage());
            }else if(throwable instanceof BadSqlGrammarException){

                taskResult.setErrorMsg("SQL参数异常,"+throwable.getMessage());
            }else if(throwable instanceof DataIntegrityViolationException){

                taskResult.setErrorMsg("数据异常,"+throwable.getMessage());
            }else {

                taskResult.setErrorMsg("未知异常,"+throwable.getMessage());
            }

            return taskResult;
        }

        if(CollectionUtils.isEmpty(sqlMetaList)){

            taskResult.setCode(SyncConstant.TASK_CODE_FAIL);
            taskResult.setErrorMsg("任务创建异常");
            taskResult.setRetry(true);

            return taskResult;
        }

        // 封装mq消息为map
        Map<String, MsgTableMeta> metaMap = new HashMap<>();

        for (MsgTableMeta msgTableMeta : msgMeta.getTableMetaList()) {

            metaMap.put(msgTableMeta.getCode(),msgTableMeta);
        }

        for (SqlMeta sqlMeta : sqlMetaList) {


            if(SyncConstant.OPERATE_TYPE_SELECT.equalsIgnoreCase(sqlMeta.getSqlType())){


                // 既然发了同步计划,一定要有同步的数据,否则视为异常
                if(CollectionUtils.isEmpty(sqlMeta.getResultList())){

                    taskResult.setCode(SyncConstant.TASK_CODE_FAIL);
                    taskResult.setErrorMsg(sqlMeta.getTableCode()+"中源表数据不存在");
                    taskResult.setRetry(true);

                    return taskResult;
                }

                // 查询出来的结果集应该大于等于消息中的uniqueKey的数据
                if(sqlMeta.getParamList().get(0).length > sqlMeta.getResultList().size()){

                    List<Object> resultList = new ArrayList<>();
                    for (Map<String, Object> resultMap : sqlMeta.getResultList()) {

                        resultList.add(resultMap.get(sqlMeta.getUniqueKey()));
                    }

                    List<Object> paramList = ListUtils.subtract(resultList, Arrays.asList(sqlMeta.getParamList().get(0)));


                    // 可能依赖的数据还没有从其他地方同步过来,等待下次重试

                    taskResult.setCode(SyncConstant.TASK_CODE_FAIL);
                    taskResult.setErrorMsg(sqlMeta.getTableCode()+"中源表数据不存在,unique:["+ StringUtils.join(paramList,",")+"]");
                    taskResult.setRetry(true);

                    return taskResult;
                }
            }else {

                MsgTableMeta msgTableMeta = metaMap.get(sqlMeta.getTableCode());

                if(null !=msgTableMeta && null !=msgTableMeta.getAffectNum() && msgTableMeta.getAffectNum() >0 && sqlMeta.getAffectNum().intValue()!=msgTableMeta.getAffectNum().intValue()){

                    taskResult.setCode(SyncConstant.TASK_CODE_FAIL);
                    taskResult.setErrorMsg("数据库操作实际影响行数与与消息中预估不一致,["+sqlMeta.getAffectNum()+","+msgTableMeta.getAffectNum()+"],语句->"+sqlMeta.getSql());
                    taskResult.setRetry(true);

                    return taskResult;
                }
            }

        }

        taskResult.setCode(SyncConstant.TASK_CODE_SUCCESS);
        taskResult.setRetry(false);

        return taskResult;
    }
}
