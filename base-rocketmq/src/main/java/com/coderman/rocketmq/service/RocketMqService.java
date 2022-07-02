package com.coderman.rocketmq.service;

/**
 * @author coderman
 * @Title: rocketMq 服务
 * @date 2022/6/2222:12
 */
public interface RocketMqService {

    /**
     * 发送同步消息
     *
     * 当发送的消息很重要是，且对响应时间不敏感的时候采用sync方式;
     * start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true
     *
     * @param tag 消息标签
     * @param msg 消息内容
     */
    public void sendMsg(String tag,String msg);




    /**
     * 消息重试
     *
     */
    public void resendMsg();

}
