package com.coderman.log.alarm;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * UnsynchronizedAppenderBase 用于异步处理，不阻塞主线程, 拦截error日志，并发送到钉钉
 */
public class DingAlarmAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private String accessKeyId;

    private String accessKeySecret;

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

                stringBuilder.append("\n报错时间:").append("\n").append(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now()));
                stringBuilder.append("\n异常类型:").append("\n").append(throwableProxy.getClassName()).append(throwableProxy.getMessage()).append("\n");

                StackTraceElementProxy[] stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
                if (stackTraceElementProxyArray != null && stackTraceElementProxyArray.length > 0) {

                    for (int i = 0; i < stackTraceElementProxyArray.length; i++) {

                        if (i > 3) {
                            break;
                        }

                        stringBuilder.append(stackTraceElementProxyArray[i].getSTEAsString()).append("\n");
                    }
                }
            }

            AlarmMessage alarmMessage = new AlarmMessage();
            alarmMessage.setAlarmName(iLoggingEvent.getMessage());
            alarmMessage.setAlarmType(AlarmTypeEnum.DING_TALK);
            alarmMessage.setMessage(stringBuilder.toString());
            alarmMessage.setAccessToken(this.accessKeyId);
            alarmMessage.setAccessKeySecret(this.accessKeySecret);
            alarmMessage.setAlarmTimeInterval(this.alarmTimeInterval);

            // 异步发送
            AlarmContext.add2Queue(alarmMessage);

        } catch (Exception exception) {

            this.addError("add to alarm queue error:" + exception.getMessage());
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
