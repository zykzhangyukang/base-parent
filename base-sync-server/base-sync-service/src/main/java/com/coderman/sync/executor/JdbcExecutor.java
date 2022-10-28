package com.coderman.sync.executor;

import com.alibaba.fastjson.JSON;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.exception.SyncException;
import com.coderman.sync.sql.meta.SqlMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
public class JdbcExecutor extends AbstractExecutor {


    @Override
    public List<SqlMeta> execute() throws Throwable {

        final JdbcTemplate jdbcTemplate = super.getJdbcTemplate();


        if(null == jdbcTemplate){

            throw new SyncException("找不到jdbcTemplate");
        }

        for (SqlMeta sqlMeta : super.getSqlList()) {


            if(SyncConstant.OPERATE_TYPE_SELECT.equalsIgnoreCase(sqlMeta.getSqlType())){

                if(sqlMeta.getParamList().size()!=1){

                    throw new SyncException("sql错误");
                }

                try {

                    // 查询数据
                    List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sqlMeta.getSql(), sqlMeta.getParamList().get(0));
                    sqlMeta.setResultList(resultList);

                    log.info("执行SQL语句返回结果:{}", JSON.toJSONString(resultList));

                }catch (Exception e){

                    log.error("执行sql语句错误:{}",e.getMessage(),e);
                    throw new RuntimeException(String.format("查询数据错误:,计划编号:%s",sqlMeta.getTableCode()));
                }

            }

        }


        return super.getSqlList();
    }
}
