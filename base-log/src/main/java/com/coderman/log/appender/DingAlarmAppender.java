package com.coderman.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.coderman.log.context.AlarmContext;
import com.coderman.log.entity.AlarmMessage;
import com.coderman.log.entity.AlarmTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * UnsynchronizedAppenderBase 用于异步处理，不阻塞主线程, 拦截error日志，并发送到钉钉
 * 优化了时间格式、异常堆栈处理和异常捕获的细节。
 * @author coderman
 */
public class DingAlarmAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    @ApiModelProperty(value = "钉钉发送消息key")
    private String accessKeyId;

    @ApiModelProperty(value = "钉钉发送消息秘钥")
    private String accessKeySecret;

    @ApiModelProperty(value = "告警时间间隔")
    private Integer alarmTimeInterval;

    /**
     * 发送告警
     *
     * @param iLoggingEvent 日志元数据
     */
    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(iLoggingEvent.getFormattedMessage()).append("\n");

            IThrowableProxy throwableProxy = iLoggingEvent.getThrowableProxy();
            if (Objects.nonNull(throwableProxy)) {
                // 添加报错时间
                stringBuilder.append("\n报错时间:").append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

                // 添加异常类型和异常信息
                stringBuilder.append("\n异常类型:").append(throwableProxy.getClassName()).append("\n");
                stringBuilder.append("异常信息:").append(throwableProxy.getMessage()).append("\n");

                // 添加堆栈信息（仅前4行）
                StackTraceElementProxy[] stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
                if (stackTraceElementProxyArray != null && stackTraceElementProxyArray.length > 0) {
                    stringBuilder.append("堆栈信息:\n");
                    for (int i = 0; i < stackTraceElementProxyArray.length && i < 4; i++) {
                        stringBuilder.append(stackTraceElementProxyArray[i].getSTEAsString()).append("\n");
                    }
                }
            }

            // 构建告警消息
            AlarmMessage alarmMessage = new AlarmMessage();
            alarmMessage.setAlarmName(iLoggingEvent.getMessage());
            alarmMessage.setAlarmType(AlarmTypeEnum.DING_TALK);
            alarmMessage.setMessage(stringBuilder.toString());
            alarmMessage.setAccessToken(this.accessKeyId);
            alarmMessage.setAccessKeySecret(this.accessKeySecret);
            alarmMessage.setAlarmTimeInterval(this.alarmTimeInterval);

            // 异步发送
            AlarmContext.addToQueue(alarmMessage);

        } catch (Exception exception) {
            // 捕获异常并输出详细的错误信息
            this.addError("Error while adding alarm to queue", exception);
        }
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public Integer getAlarmTimeInterval() {
        return alarmTimeInterval;
    }

    public void setAlarmTimeInterval(Integer alarmTimeInterval) {
        this.alarmTimeInterval = alarmTimeInterval;
    }
}
