package com.coderman.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;

/**
 * @author coderman
 * @Title: 日志工具类
 * @Description: TOD
 * @date 2022/6/1918:15
 */
public class LoggerUtils {

    public static final String TRACE = "TRACE";

    public static final String INFO = "INFO";

    public static final String DEBUG = "DEBUG";

    public static final String WARN = "WARN";

    public static final String ERROR = "ERROR";

    public static final String pattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * Print if log debug level is enabled.
     *
     * @param logger logger
     * @param s      log message
     * @param args   arguments
     */
    public static void printIfDebugEnabled(Logger logger, String s, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(s, JSON.toJSONStringWithDateFormat(args, pattern));
        }
    }

    /**
     * Print if log info level is enabled.
     *
     * @param logger logger
     * @param s      log message
     * @param args   arguments
     */
    public static void printIfInfoEnabled(Logger logger, String s, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(s, JSON.toJSONStringWithDateFormat(args, pattern));
        }
    }

    /**
     * Print if log trace level is enabled.
     *
     * @param logger logger
     * @param s      log message
     * @param args   arguments
     */
    public static void printIfTraceEnabled(Logger logger, String s, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(s, JSON.toJSONStringWithDateFormat(args, pattern));
        }
    }

    /**
     * Print if log warn level is enabled.
     *
     * @param logger logger
     * @param s      log message
     * @param args   arguments
     */
    public static void printIfWarnEnabled(Logger logger, String s, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(s, JSON.toJSONStringWithDateFormat(args, pattern));
        }
    }

    /**
     * Print if log error level is enabled.
     *
     * @param logger logger
     * @param s      log message
     * @param args   arguments
     */
    public static void printIfErrorEnabled(Logger logger, String s, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(s, JSON.toJSONStringWithDateFormat(args, pattern));
        }
    }
}
