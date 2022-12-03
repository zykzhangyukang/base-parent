package com.coderman.sync.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

@Lazy(value = false)
@Component
public class SyncLifecycle implements SmartLifecycle {

    private final static Logger logger = LoggerFactory.getLogger(SyncLifecycle.class);


    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private DataSourceInitializer dataSourceInitializer;


    @Resource
    private SyncPlanInitializer syncPlanInitializer;

    @Override
    public void start() {

        if (this.initialized.compareAndSet(false, true)) {

            logger.info("初始化数据源...");

            this.dataSourceInitializer.init();

            logger.info("初始化数据源结束...");


            logger.info("初始化同步计划");

            this.syncPlanInitializer.init();

            logger.info("初始化同步计划结束");
        }

    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop() {
        this.initialized.getAndSet(false);
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return this.initialized.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
