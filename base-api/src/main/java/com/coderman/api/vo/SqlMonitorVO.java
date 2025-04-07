package com.coderman.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author ：zhangyukang
 * @date ：2025/04/07 16:03
 */
@Data
public class SqlMonitorVO {
    /**
     * SQL
     */
    private String SQL;
    /**
     * HASH值
     */
    private String Hash;
    /**
     * 最大耗时
     */
    private Long MaxTimespan;
    /**
     * 最大耗时sql出现的时间
     */
    private Date MaxTimespanOccurTime;
    /**
     * 最大读取行数
     */
    private Long FetchRowCountMax;
    /**
     * 最大更新行数
     */
    private Long EffectedRowCountMax;
    /**
     * 错误数
     */
    private Long ErrorCount;
}
