package com.coderman.sync.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：zhangyukang
 * @date ：2024/08/12 14:10
 */
@ConfigurationProperties(prefix = "sync")
@Configuration
@Data
public class SyncProperties {

    @ApiModelProperty(value = "队列类型")
    private String mq;

    @ApiModelProperty(value = "rocketmq配置")
    private final SyncProperties.RocketMQ rocketmq = new SyncProperties.RocketMQ();

    @ApiModelProperty(value = "activemq配置")
    private final SyncProperties.ActiveMQ activemq = new SyncProperties.ActiveMQ();

    @Data
    public static class RocketMQ {

        @ApiModelProperty(value = "实例名称")
        private String instantName;

        @ApiModelProperty(value = "namesrv地址")
        private String namesrvAddr;

        @ApiModelProperty(value = "发送超时时间")
        private int sendMsgTimeoutMillis;

        @ApiModelProperty(value = "消息重试次数")
        private int retryTimes;

        @ApiModelProperty(value = "同步消息主题")
        private String syncTopic;

        @ApiModelProperty(value = "同步消息主题[顺序]")
        private String syncOrderTopic;

        @ApiModelProperty(value = "生产者组")
        private String producerGroup;

        @ApiModelProperty(value = "生产者主[顺序]")
        private String producerOrderGroup;
    }

    @Data
    public static class ActiveMQ {

        @ApiModelProperty(value = "队列名称")
        private String queueName;

        @ApiModelProperty(value = "broker地址")
        private String brokerUrl;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "密码")
        private String password;
    }
}
