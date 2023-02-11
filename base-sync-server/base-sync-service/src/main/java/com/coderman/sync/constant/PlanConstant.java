package com.coderman.sync.constant;

import com.coderman.api.anntation.ConstList;

public interface PlanConstant {

    /**
     * 计划状态
     */
    @ConstList(group = "plan_status",name = "正常")
    public static final String STATUS_NORMAL = "normal";
    @ConstList(group = "plan_status",name = "禁用")
    public static final String STATUS_FORBID = "forbid";


    /**
     * 分割符标识: #,+
     */
    public static final String SPLIT_FLAG_POUND = "#";
    public static final String split_flag_plus = "+";

    /**
     * 操作结果
     */
    @ConstList(group = "result_status",name = "成功")
    public static String RESULT_STATUS_SUCCESS = "success";
    @ConstList(group = "result_status",name = "失败")
    public static String RESULT_STATUS_FAIL = "fail";


    /**
     * 消息来源
     */
    @ConstList(group = "msg_source",name = "MQ")
    public static final String MSG_ROCKET_MQ = "rocket_mq";
    @ConstList(group = "msg_source",name = "定时器补偿")
    public static final String MSG_SOURCE_JOB = "job";


    /**
     * 回调状态
     */
    public static final String CALLBACK_STATUS_WAIT = "wait";
    public static final String CALLBACK_STATUS_ING = "ing";
    public static final String CALLBACK_STATUS_FAIL = "fail";
    public static final String CALLBACK_STATUS_SUCCESS = "success";
}
