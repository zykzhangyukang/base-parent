package com.coderman.sync.context;

import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.callback.CallBackExecutor;
import com.coderman.sync.callback.meta.CallbackTask;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@Lazy(false)
@DependsOn("springContextUtil")
public class CallbackContext {


    private static CallbackContext callbackContext;


    @Resource
    private CallBackExecutor callBackExecutor;


    @PostConstruct
    private void  init(){

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

    private void addTask(CallbackTask callbackTask) {


        this.callBackExecutor.addTask(callbackTask);
    }
}
