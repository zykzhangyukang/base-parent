package com.coderman.log.filter;

import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author coderman
 */
public class AlarmFilter extends LevelFilter {

    String LOG_SYS_FAIL = " Controller统一异常处理";
    String LOG_SYS_CLASS = "com.coderman.service.aop.GlobalExceptionHandler";

    @Override
    public FilterReply decide(ILoggingEvent event) {

        String loggerName = event.getLoggerName();
        String message = event.getFormattedMessage();

        FilterReply decide = super.decide(event);

        // 如果等级匹配上了, 要进一步匹配是否由全局异常的日志
        if (FilterReply.ACCEPT.equals(decide)) {

            if (LOG_SYS_CLASS.equalsIgnoreCase(loggerName) &&  message !=null && message.contains(LOG_SYS_FAIL)) {

                return FilterReply.ACCEPT;
            } else {

                return FilterReply.DENY;
            }

        }
        return decide;
    }
}
