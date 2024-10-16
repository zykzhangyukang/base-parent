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

            StringBuilder logMessage = new StringBuilder();
            logMessage.append("===== Log Start =====").append("\n");

            logMessage.append("Message: ").append(iLoggingEvent.getFormattedMessage()).append("\n");

            IThrowableProxy throwableProxy = iLoggingEvent.getThrowableProxy();
            if (Objects.nonNull(throwableProxy)) {
                String errorTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                logMessage.append("\n=== Error Information ===").append("\n")
                        .append("Error Timestamp: ").append(errorTime).append("\n");

                logMessage.append("Exception Type: ").append(throwableProxy.getClassName()).append("\n")
                        .append("Exception Message: ").append(throwableProxy.getMessage()).append("\n");

                StackTraceElementProxy[] stackTraceElements = throwableProxy.getStackTraceElementProxyArray();
                if (stackTraceElements != null && stackTraceElements.length > 0) {
                    StackTraceElement firstElement = stackTraceElements[0].getStackTraceElement();
                    logMessage.append("Error Occurred at: ")
                            .append(firstElement).append(" (Line: ")
                            .append(firstElement.getLineNumber()).append(")\n");

                    logMessage.append("\n--- Stack Trace (Top 10) ---").append("\n");
                    for (int i = 0; i < Math.min(10, stackTraceElements.length); i++) {
                        StackTraceElement element = stackTraceElements[i].getStackTraceElement();
                        logMessage.append("\t").append(element);
                    }
                }
            }

            logMessage.append("===== Log End =====").append("\n");

            // 构建告警消息
            AlarmMessage alarmMessage = new AlarmMessage();
            alarmMessage.setAlarmName(iLoggingEvent.getFormattedMessage());
            alarmMessage.setAlarmType(AlarmTypeEnum.DING_TALK);
            alarmMessage.setMessage(logMessage.toString());
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
