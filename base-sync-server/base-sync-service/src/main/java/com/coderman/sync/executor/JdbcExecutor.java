package com.coderman.sync.executor;

import com.alibaba.fastjson.JSON;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.exception.ErrorCodeEnum;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.plan.meta.MsgTableMeta;
import com.coderman.sync.sql.meta.SqlMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JdbcExecutor extends AbstractExecutor {


    @Override
    public List<SqlMeta> execute() throws Throwable {

        final JdbcTemplate jdbcTemplate = super.getJdbcTemplate();


        if (null == jdbcTemplate) {

            throw new SyncException(ErrorCodeEnum.DB_NOT_CONNECT);
        }

        // 非查询集合
        final List<SqlMeta> noSelectList = new ArrayList<>();

        for (SqlMeta sqlMeta : super.getSqlList()) {


            // 处理查询语句
            if (SyncConstant.OPERATE_TYPE_SELECT.equalsIgnoreCase(sqlMeta.getSqlType())) {

                if (sqlMeta.getParamList().size() != 1) {

                    throw new SyncException(ErrorCodeEnum.SQL_PARAM_EXCEED);
                }

                try {

                    // 打印sql日志
                    this.printLog(sqlMeta.getSql(), sqlMeta.getParamList());

                    // 查询数据
                    List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sqlMeta.getSql(), sqlMeta.getParamList().get(0));
                    sqlMeta.setResultList(resultList);

                    log.info("执行SQL语句返回结果:{}", JSON.toJSONString(resultList));

                } catch (Exception e) {

                    log.error("", e);
                    throw new RuntimeException(String.format("sql:%s,destDb:%s,error:%s", sqlMeta.getSql(),
                            CollectionUtils.isEmpty(sqlMeta.getCallbackTaskList()) ? null : (sqlMeta.getCallbackTaskList().get(0).getProject() + ":" + sqlMeta.getCallbackTaskList().get(0).getDb()), e), e);
                }

            } else {


                if (super.getMsgMeta() != null) {


                    for (MsgTableMeta tableMeta : super.getMsgMeta().getTableMetaList()) {

                        if (tableMeta.getCode().equals(sqlMeta.getTableCode())) {

                            sqlMeta.setAffectNum(tableMeta.getAffectNum());
                            break;
                        }
                    }
                }

                // 封装 insert,update,delete 语句
                noSelectList.add(sqlMeta);

            }
        }

        if (CollectionUtils.isNotEmpty(noSelectList)) {


            TransactionTemplate transactionTemplate = super.getTransactionTemplate();

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {


                    for (SqlMeta meta : noSelectList) {

                        int[] affects;

                        try {

                            // 打印sql日志
                            printLog(meta.getSql(), meta.getParamList());

                            affects = jdbcTemplate.batchUpdate(meta.getSql(), meta.getParamList(), meta.getArgTypes());

                        } catch (Exception e) {

                            if (e instanceof DuplicateKeyException) {

                                throw new SyncException(ErrorCodeEnum.DB_KEY_DUPLICATE, "键值重复," + e.getMessage());
                            }

                            throw new RuntimeException(String.format("tableCode:%s,msg:%s,目标库:%s,error:%s", meta.getTableCode(), CollectionUtils.isEmpty(meta.getCallbackTaskList()) ?
                                    meta.getUniqueKey() : meta.getCallbackTaskList().get(0).getMsg(), CollectionUtils.isEmpty(meta.getCallbackTaskList()) ? null : (meta.getCallbackTaskList().get(0).getProject() + ":" + meta.getCallbackTaskList().get(0).getDb()), e), e);
                        }

                        int count = 0;

                        for (int affect : affects) {

                            // oracle对于成功的批处理,该数据包含了所有的-2,1或正整数表示
                            if (affect == -2) {

                                affect = 1;
                            }

                            if (affect < 0) {

                                affect = affect * -1;
                            }

                            count += affect;
                        }

                        if (meta.getAffectNum() != null && meta.getAffectNum() > count) {

                            throw new RuntimeException(String.format("计划编号:%s,预计受影响行数:%s,实际受影响行数:%s", meta.getTableCode(), meta.getAffectNum(), count));
                        }

                        meta.setAffectNum(count);
                    }
                }
            });
        }


        return super.getSqlList();
    }


    private void printLog(String sql, List<Object[]> paramList) {


        if (log.isInfoEnabled()) {


            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("执行sql语句->");
            stringBuilder.append(sql);

            for (Object[] objects : paramList) {

                stringBuilder.append("[")
                        .append(StringUtils.join(objects, ","))
                        .append("]");
            }

            log.info(stringBuilder.toString());
        }

    }
}
