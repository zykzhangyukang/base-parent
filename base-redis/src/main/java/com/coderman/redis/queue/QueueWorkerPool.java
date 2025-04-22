package com.coderman.redis.queue;

import com.coderman.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueWorkerPool {

    private final RedisService redisService;
    private final ApplicationContext applicationContext;

    private final ExecutorService taskExecutor = Executors.newFixedThreadPool(1); // 消费线程池
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(); // 调度线程

    private final ConcurrentMap<String, QueueMessageHandler> handlerMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, QueueConfig> queueConfigMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        Map<String, Object> listeners = applicationContext.getBeansWithAnnotation(QueueListener.class);
        for (Object listener : listeners.values()) {
            QueueListener ann = AnnotationUtils.findAnnotation(listener.getClass(), QueueListener.class);
            if (ann == null) continue;

            String queue = ann.queue();
            handlerMap.put(queue, (QueueMessageHandler) listener);
            queueConfigMap.put(queue, new QueueConfig(queue, ann.maxRetries(), ann.retryDelay()));

            log.info("Registered QueueListener for queue [{}]", queue);
        }

        scheduler.scheduleAtFixedRate(this::pollQueues, 0, 300, TimeUnit.MILLISECONDS);
        log.info("Started polling scheduler");
    }

    private void pollQueues() {
        for (String queue : handlerMap.keySet()) {
            String queueKey = "rqueue:queue:" + queue;
            try {
                QueueMessage msg = redisService.rightPopList(queueKey, QueueMessage.class, 0);
                if (msg != null) {
                    taskExecutor.submit(new QueueWorker(queue, msg, handlerMap.get(queue), queueConfigMap.get(queue), redisService));
                }
            } catch (Exception e) {
                log.error("Failed to poll queue [{}]: {}", queue, e.getMessage(), e);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down QueueWorkerPool...");
        scheduler.shutdown();
        taskExecutor.shutdown();
        try {
            if (!taskExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Forcing taskExecutor shutdown...");
                taskExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
