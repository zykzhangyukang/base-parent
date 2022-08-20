package com.coderman.service.aspect;


import com.coderman.api.constant.CommonConstant;
import com.coderman.service.anntation.LogError;
import com.coderman.service.anntation.LogErrorParam;
import com.coderman.service.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
@Slf4j
@Order(value = 2500)
public class ExceptionAspect {


    private Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    @Pointcut("(execution(* com.coderman..service..*(..))) && @within(org.springframework.stereotype.Service)")
    public void serviceAspect() {
    }


    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Throwable e) {


        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();


        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();


        String logErrorMsg = StringUtils.EMPTY;

        // 获取注解
        LogError logError = method.getAnnotation(LogError.class);

        if (logError != null) {

            logErrorMsg = logError.value();
            String logParam = getLogParam(point, method);

            logErrorMsg = logErrorMsg + (StringUtils.isNotBlank(logParam) ? "参数信息:" + logParam : "");

        } else {

            StackTraceElement[] stackTraceElements = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {

                try {
                    Class<?> stackTraceClass = Class.forName(stackTraceElement.getClassName());
                    String pkgName = stackTraceClass.getPackage().getName();

                    if (StringUtils.isNotEmpty(pkgName) && pkgName.startsWith(CommonConstant.BASE_PACKAGE)) {

                        Method[] methods = stackTraceClass.getMethods();
                        for (Method methodTmp : methods) {

                            if (stackTraceElement.getMethodName().equals(methodTmp.getName()) && methodTmp.isAnnotationPresent(LogError.class)) {

                                logError = methodTmp.getAnnotation(LogError.class);
                                break;
                            }

                        }
                    }

                } catch (Exception ignored) {

                }
            }
        }


        if (StringUtils.isNotBlank(logErrorMsg)) {

            logErrorMsg = "Service统一异常处理,LogError: " + logErrorMsg;
        }


        if (logger.isInfoEnabled()) {

            String builder = "[ExceptionAspect][AfterThrowing][Begin]\n" +
                    "[FullUrl-->" + request.getRequestURI() + "]\n" +
                    "[ReqUrl-->" + request.getRequestURI() + "]\n" +
                    "[ReqUrl-->" + request.getServletPath() + "]\n" +
                    "[Ip-->" + request.getRemoteHost() + "]\n" +
                    "[MethodName-->" + point.getSignature().getName() + "]\n" +
                    "[CreateTime-->" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "]\n" +
                    "[ExceptionClassName-->" + point + "]\n" +
                    "[ExceptionMsg-->" + ExceptionUtils.getMessage(e) + "]\n" +
                    "[ExceptionCause-->" + ExceptionUtils.getCause(e) + "]\n" +
                    "[ExceptionName-->" + e.getClass().getName() + "]\n" +
                    "[ClassName-->" + point.getTarget().getClass() + "]\n" +
                    "[Execution-->" + point + "]\n" +
                    "[ExceptionAspect][AfterThrowing][End]";


            logger.info(builder);
        }


        boolean printException = true;

        if (logError != null) {

            Class<?>[] notPrintExceptionClazz = logError.notPrintException();

            for (Class<?> clazz : notPrintExceptionClazz) {

                if (e.getClass().equals(clazz)) {
                    printException = false;
                    break;
                }
            }
        }


        if (printException) {

            logger.error(logErrorMsg, e);
        } else {

            logger.error(logErrorMsg);
        }

    }

    private String getLogParam(JoinPoint point, Method method) {

        StringBuilder builder = new StringBuilder();
        Object[] args = point.getArgs();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {

            Parameter parameter = parameters[i];
            Object paramArg = args[i];

            LogErrorParam logErrorParam = parameter.getDeclaredAnnotation(LogErrorParam.class);

            if (logErrorParam == null) {
                continue;
            }


            String[] logFields = logErrorParam.value();

            if (logFields.length == 0) {
                builder.append("param[").append(parameter.getName()).append("] value[").append(paramArg).append("]");
                continue;
            }


            for (String filed : logFields) {

                Field field = ReflectUtil.getField(paramArg.getClass(), filed);
                if (field == null) {
                    continue;
                }

                field.setAccessible(true);

                try {
                    builder.append("param[").append(field.getName()).append("] value[").append(field.get(paramArg)).append("]");
                } catch (Exception ignored) {

                }

            }

        }

        return builder.toString();
    }


}
