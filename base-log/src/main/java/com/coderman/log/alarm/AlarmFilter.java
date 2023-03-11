package com.coderman.log.alarm;

import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;
import com.coderman.log.LogConstant;

public class AlarmFilter extends LevelFilter {

    @Override
    public FilterReply decide(ILoggingEvent event) {

        String loggerName = event.getLoggerName();
        String message = event.getFormattedMessage();

        FilterReply decide = super.decide(event);

        // 如果等级匹配上了, 要进一步匹配是否由全局异常的日志
        if(FilterReply.ACCEPT.equals(decide)){

            if (LogConstant.LOG_SYS_CLASS.equalsIgnoreCase(loggerName) &&  message !=null && message.contains(LogConstant.LOG_SYS_FAIL)) {

                return FilterReply.ACCEPT;
            }else {

                return FilterReply.DENY;
            }

        }
        return decide;
    }
}
