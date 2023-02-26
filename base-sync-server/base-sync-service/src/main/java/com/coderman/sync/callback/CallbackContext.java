package com.coderman.sync.callback;

import com.alibaba.fastjson.JSON;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.callback.meta.CallbackTask;
import com.coderman.sync.thread.CallbackRetryThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@Lazy(false)
@Slf4j
@DependsOn("springContextUtil")
public class CallbackContext {

    private static CallbackContext callbackContext;

    @Resource
    private CallBackExecutor callBackExecutor;


    @Resource
    private CallbackRetryThread callbackRetryThread;


    @PostConstruct
    private void init() {

        CallbackContext.callbackContext = SpringContextUtil.getBean(CallbackContext.class);
    }

    public static CallbackContext getCallbackContext() {
        return callbackContext;
    }

    public static void setCallbackContext(CallbackContext callbackContext) {
        CallbackContext.callbackContext = callbackContext;
    }

    public void addTask(List<CallbackTask> callbackTaskList) {

        for (CallbackTask callbackTask : callbackTaskList) {

            this.addTask(callbackTask);
        }
    }

    public void addTask(CallbackTask callbackTask) {


        this.callBackExecutor.addTask(callbackTask);
    }

    public void addTaskToDelayQueue(CallbackTask callbackTask) {

        if (callbackTask.getRetryTimes() == 0) {

            callbackTask.setDelayTime(10);

        } else if (callbackTask.getRetryTimes() == 1) {

            callbackTask.setDelayTime(30);

        } else if (callbackTask.getRetryTimes() == 2) {

            callbackTask.setDelayTime(60);

        } else {

            log.error("超过重试次数,禁止重试，callbackTask:{}", JSON.toJSONString(callbackTask));
            return;
        }

        this.callbackRetryThread.add(callbackTask);
    }
}
