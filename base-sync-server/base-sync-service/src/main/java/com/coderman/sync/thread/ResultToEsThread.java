package com.coderman.sync.thread;

import com.coderman.sync.es.EsService;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.task.base.BaseTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Lazy(false)
public class ResultToEsThread {


    private static final Queue<ResultModel> queue = new ConcurrentLinkedQueue<>();

    private static volatile boolean loop = true;

    @Resource
    private EsService esService;


    public void add(ResultModel resultModel) {
        queue.add(resultModel);
    }

    @PostConstruct
    public void init() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                List<ResultModel> list = new ArrayList<>();

                while (loop) {

                    try {

                        insertBatchRecord(list);

                        if (CollectionUtils.isNotEmpty(list)) {

                            esService.batchInsertSyncResult(list);
                        }

                        list.clear();

                        TimeUnit.SECONDS.sleep(3);

                    } catch (Exception e) {

                        log.error("同步结果到ES失败:{}", e.getMessage());

                        try {
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException ignored) {
                        }
                    }

                }
            }
        });

        thread.setDaemon(true);
        thread.setName("同步ES线程");
        thread.start();

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {

                if ("同步ES线程".equals(t.getName())) {

                    init();
                }
            }
        });

        log.info("同步ES线程启动...");
    }

    private void insertBatchRecord(List<ResultModel> list) throws IOException {

        ResultModel resultModel;

        while (null != (resultModel = queue.poll())) {

            list.add(resultModel);

            if (list.size() > 50) {

                this.esService.batchInsertSyncResult(list);

                list.clear();
            }
        }
    }

    @PreDestroy
    private void flushInfo() throws Exception {

        log.error("系统关闭,处理内存数据...");

        loop = false;

        Thread.sleep(1200);

        List<ResultModel> list =  new ArrayList<>();

        this.insertBatchRecord(list);

        if(CollectionUtils.isNotEmpty(list)){

            this.esService.batchInsertSyncResult(list);
        }
    }
}
