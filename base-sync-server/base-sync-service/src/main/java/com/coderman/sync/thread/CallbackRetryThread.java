package com.coderman.sync.thread;

import com.coderman.sync.callback.meta.CallbackTask;
import com.coderman.sync.context.CallbackContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;

@Component
@Lazy(false)
@Slf4j
public class CallbackRetryThread {


    private final DelayQueue<CallbackTask> queue = new DelayQueue<>();


    public void add(CallbackTask callbackTask){

        this.queue.add(callbackTask);
    }

    @PostConstruct
    public void init(){

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true){

                    try {

                        CallbackTask callbackTask = queue.take();

                        callbackTask.setFirst(false);

                        CallbackContext.getCallbackContext().addTask(callbackTask);

                    } catch (InterruptedException e) {

                        throw new RuntimeException(e);
                    }

                }

            }

        });

        thread.setName("回调失败重试线程");
        thread.setDaemon(true);
        thread.start();

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {

                if("回调失败重试线程".equals(t.getName())){

                    init();
                }
            }
        });

        log.info("回调重试线程启动...");
    }

}
