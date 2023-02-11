package com.coderman.sync.thread;

import com.coderman.sync.context.SyncContext;
import com.coderman.sync.task.base.BaseTask;
import com.coderman.sync.task.base.MsgTask;
import com.coderman.sync.task.support.WriteBackTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Lazy(false)
@Slf4j
public class SyncRetryThread {


    private final DelayQueue<BaseTask> queue = new DelayQueue<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);


    private void process(BaseTask baseTask){

        if (baseTask instanceof WriteBackTask) {

            ((WriteBackTask) baseTask).process();

        } else if (baseTask instanceof MsgTask) {

            MsgTask msgTask = (MsgTask) baseTask;
            SyncContext.getContext().syncData(msgTask.getMsg(), StringUtils.EMPTY, msgTask.getSource(), 0);
        }

    }

    public void addTask(BaseTask baseTask) {
        this.queue.add(baseTask);
    }

    @PostConstruct
    public void init(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    try {

                        final BaseTask task = queue.take();

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    process(task);

                                } catch (Throwable throwable) {

                                    SyncContext.getContext().addTaskToDelayQueue(task);
                                }

                            }
                        });


                    } catch (InterruptedException e) {


                        throw new RuntimeException(e);
                    }

                }

            }
        });

        thread.setName("同步失败重试线程");
        thread.setDaemon(true);
        thread.start();

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {

                if("同步失败重试线程".equals(t.getName())){

                    init();
                }
            }
        });

        log.info("同步重试线程启动...");

    }


    @PreDestroy
    private void destroy(){
        this.executorService.shutdown();
    }

}
