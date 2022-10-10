package com.coderman.sync.constant;

public interface Constant {

    /**
     * 发送状态: 发送失败,发送成功,发送中,待发送
     */
    public static final String MSG_SEND_STATUS_FAIL = "fail";
    public static final String MSG_SEND_STATUS_SUCCESS = "success";
    public static final String MSG_SEND_STATUS_SENDING = "sending";
    public static final String MSG_SEND_STATUS_WAIT = "wait";


    /**
     * 消息处理状态: 待处理,处理成功,处理失败,无需处理
     */
    public static final String MSG_DEAL_STATUS_WAIT = "wait";
    public static final String MSG_DEAL_STATUS_SUCCESS = "success";
    public static final String MSG_DEAL_STATUS_fail = "fail";
    public static final String MSG_DEAL_STATUS_NONEED = "noneed";
}
