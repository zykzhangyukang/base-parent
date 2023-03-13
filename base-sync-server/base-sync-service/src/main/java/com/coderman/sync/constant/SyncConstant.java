package com.coderman.sync.constant;

import com.coderman.api.anntation.ConstList;
import com.coderman.api.anntation.Constant;

@Constant(allowedConflict = false)
public interface SyncConstant {

    /**
     * 源系统
     */
    @ConstList(group = "src_project",name = "测试系统")
    public static final String SRC_PROJECT_DEMO= "demo";

    /**
     * 目标系统
     */
    @ConstList(group = "dest_project",name = "采购系统")
    public static final String DEST_PROJECT_PMS = "pms";
    @ConstList(group = "dest_project",name = "仓储系统")
    public static final String DEST_PROJECT_WMS= "wms";
    @ConstList(group = "dest_project",name = "营销后台")
    public static final String DEST_PROJECT_MMS = "mms";
    @ConstList(group = "dest_project",name = "购物车")
    public static final String DEST_PROJECT_CART = "cart";
    @ConstList(group = "dest_project",name = "营销前台")
    public static final String DEST_PROJECT_MARKET= "market";
    @ConstList(group = "dest_project",name = "订单系统")
    public static final String DEST_PROJECT_ORDER = "order";
    @ConstList(group = "dest_project",name = "索引系统")
    public static final String DEST_PROJECT_PIM = "pim";


    /**
     * 消息来源
     */
    @ConstList(group = "msg_source",name = "RocketMQ")
    public static final String MSG_ROCKET_MQ = "rocket_mq";
    @ConstList(group = "msg_source",name = "分区有序")
    public static final String MSG_ROCKET_ORDER_MQ = "rocket_order_mq";
    @ConstList(group = "msg_source",name = "定时器补偿")
    public static final String MSG_SOURCE_JOB = "job";
    @ConstList(group = "msg_source",name = "手动同步")
    public static final String MSG_SOURCE_HANDLE = "handle";

    /**
     * 消息重试次数
     */
    @ConstList(group = "repeat_times",name = ">=1")
    public static final Integer REPEAT_TIMES_1 = 1;
    @ConstList(group = "repeat_times",name = ">=2")
    public static final Integer REPEAT_TIMES_2 = 2;
    @ConstList(group = "repeat_times",name = ">=3")
    public static final Integer REPEAT_TIMES_3 = 3;
    @ConstList(group = "repeat_times",name = ">=4")
    public static final Integer REPEAT_TIMES_4 = 4;
    @ConstList(group = "repeat_times",name = ">=5")
    public static final Integer REPEAT_TIMES_5 = 5;
    @ConstList(group = "repeat_times",name = ">=6")
    public static final Integer REPEAT_TIMES_6= 6;


    // 数据库类型 mysql
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_MONGO = "mongo";
    public static final String DB_TYPE_MSSQL = "mssql";
    public static final String DB_TYPE_ORACLE = "oracle";


    /**
     * 消息类型: 回调,同步
     */
    String MSG_TYPE_SYNC = "sync";
    String MSG_TYPE_CALLBACK = "callback";


    /**
     * 任务执行结果
     */
    @ConstList(group = "sync_status",name = "成功")
    public static final String TASK_CODE_SUCCESS = "success";
    @ConstList(group = "sync_status",name = "失败")
    public static final String TASK_CODE_FAIL = "fail";

    /**
     * 消息处理结果
     */
    public static final String SYNC_END = "end";
    public static final String SYNC_RETRY = "retry";

    /**
     * 操作类型
     */
    String OPERATE_TYPE_SELECT = "select";
    String OPERATE_TYPE_UPDATE = "update";
    String OPERATE_TYPE_DELETE = "delete";
    String OPERATE_TYPE_INSERT = "insert";


    /**
     * 同步消息发送状态
     */
    @ConstList(group = "send_status",name = "发送成功")
    public static final String SEND_STATUS_SUCCESS = "success";
    @ConstList(group = "send_status",name = "发送失败")
    public static final String SEND_STATUS_FAIL = "fail";
    @ConstList(group = "send_status",name = "发送中")
    public static final String SEND_STATUS_SENDING = "sending";
    @ConstList(group = "send_status",name = "待发送")
    public static final String SEND_STATUS_WAIT = "wait";


    /**
     * 同步消息发送状态
     */
    @ConstList(group = "deal_status",name = "待处理")
    public static final String DEAL_STATUS_WAIT = "wait";
    @ConstList(group = "deal_status",name = "处理成功")
    public static final String DEAL_STATUS_SUCCESS = "success";
    @ConstList(group = "deal_status",name = "处理失败")
    public static final String DEAL_STATUS_FAIL = "fail";
}
