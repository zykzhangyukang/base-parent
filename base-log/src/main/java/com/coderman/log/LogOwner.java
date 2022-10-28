package com.coderman.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.util.StringUtils;

public class LogOwner extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {

        String logOwner = System.getProperty("logging.owner");

        if (!StringUtils.isEmpty(logOwner)) {

            return logOwner + "->";
        }

        return "";
    }

}
