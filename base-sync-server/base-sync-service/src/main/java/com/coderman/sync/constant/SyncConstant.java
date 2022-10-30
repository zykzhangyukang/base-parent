package com.coderman.sync.constant;

public interface SyncConstant {

    // 数据库类型 mysql
    public static final String DB_TYPE_MYSQL = "mysql";

    /**
     * 消息类型: 回调,同步
     */
    String MSG_TYPE_SYNC = "sync";
    String MSG_TYPE_CALLBACK = "callback";


    /**
     * 任务执行结果
     */
    public static final String TASK_CODE_SUCCESS = "success";
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
}
