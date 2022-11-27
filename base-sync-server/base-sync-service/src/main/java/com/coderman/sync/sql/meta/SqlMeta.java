package com.coderman.sync.sql.meta;

import com.coderman.sync.callback.meta.CallbackTask;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SqlMeta {

    /**
     * sql语句
     */
    private String sql;

    /**
     * 同步计划table编号
     */
    private String tableCode;


    /**
     * 语句类型
     */
    private String sqlType;

    /**
     * 参数集合
     */
    private List<Object[]> paramList;


    /**
     * 参数类型
     */
    private int[] argTypes = {};


    /**
     * 结果集
     */
    private List<Map<String, Object>> resultList;


    /**
     * 影响行数
     */
    private Integer affectNum;


    /**
     * 唯一关联key
     */
    private String uniqueKey;


    /**
     * 回调任务封装
     */
    private List<CallbackTask> callbackTaskList;
}
