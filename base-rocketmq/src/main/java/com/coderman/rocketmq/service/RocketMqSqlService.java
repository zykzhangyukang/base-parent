package com.coderman.rocketmq.service;

/**
 * @author coderman
 * @Title: rocketMq 服务
 * @date 2022/6/2222:12
 */
public interface RocketMqSqlService {

    /**
     * 发送同步消息通过SQL发送
     *
     * 当发送的消息很重要是，且对响应时间不敏感的时候采用sync方式;
     * start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true
     *
     * @param tag 消息标签
     * @param msg 消息内容
     */
    public void sendMsgBySql(String tag, String msg);


    // linux:
    /**
     * nohup sh mqnamesrv >/usr/local/rocketmq/logs/mqnamesrv.log 2>&1 &
     *
     * 修改配置: /config/broker.conf
     *
     * namesrvAddr=127.0.0.1:9876
     * brokerIP1=192.168.200.130#是大写的IP一定要注意！！！修改成自己的IP地址
     *
     *
     * nohup sh mqbroker -n localhost:9876 -c /usr/local/rocketmq/conf/broker.conf >/usr/local/rocketmq/logs/broker.log 2>&1 &
     *
     */


    /**
     * 消息重试
     *
     */
    public void resendMsg();

}
