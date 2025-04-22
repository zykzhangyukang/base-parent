package com.coderman.redis.queue;

import com.coderman.redis.annotaion.QueueListener;
import com.coderman.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Initializes and manages QueueWorker threads for handling Redis-based message queues.
 * Supports multiple queue listeners and multi-threaded consumption per queue.
 * Thread-safe and supports graceful shutdown.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QueueWorkerPool {

    private final RedisService redisService;
    private final ApplicationContext applicationContext;

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    private final Queue<QueueWorker> workers = new ConcurrentLinkedQueue<>();

    @PostConstruct
    public void init() {
        Map<String, Object> listeners = applicationContext.getBeansWithAnnotation(QueueListener.class);
        for (Object listener : listeners.values()) {
            QueueListener ann = AnnotationUtils.findAnnotation(listener.getClass(), QueueListener.class);

            if (ann == null) continue;

            String queue = ann.queue();
            int maxRetries = ann.maxRetries();
            long retryDelay = ann.retryDelay();
            int threadCount = ann.threadCount();

            for (int i = 0; i < threadCount; i++) {
                QueueWorker worker = new QueueWorker(queue, (QueueMessageHandler) listener, redisService, maxRetries, retryDelay);
                workers.add(worker);
                executor.submit(worker);
                log.info("Started QueueWorker for queue [{}] thread [{}]", queue, i);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down QueueWorkerPool...");
        for (QueueWorker worker : workers) {
            worker.shutdown();
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Force shutting down QueueWorker executor...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
