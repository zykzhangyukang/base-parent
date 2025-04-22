package com.coderman.redis.queue;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 10:59
 */
@RequiredArgsConstructor
@Slf4j
public class QueueWorker implements Runnable {

    private final String queue;
    private final QueueMessageHandler handler;
    private final RedisService redisService;
    private final int maxRetries;
    private final long retryDelay;

    private volatile boolean running = true;

    public void shutdown() {
        running = false;
    }

    @Override
    public void run() {
        String queueKey = "rqueue:queue:" + queue;
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                QueueMessage msg = redisService.rightPopList(queueKey, QueueMessage.class, RedisDbConstant.REDIS_DB_DEFAULT);
                if (msg == null){
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }

                try {
                    handler.handle(msg);
                    log.debug("Processed message: {}", msg.getId());
                } catch (Exception ex) {
                    log.warn("Handler error on message {}: {}", msg.getId(), ex.getMessage(), ex);
                    if (msg.getRetryCount() < maxRetries) {
                        msg.setRetryCount(msg.getRetryCount() + 1);
                        long score = System.currentTimeMillis() + retryDelay;
                        redisService.zSetAdd("rqueue:delayed:" + queue, msg, score, RedisDbConstant.REDIS_DB_DEFAULT);
                        log.warn("Message {} retry #{} delayed", msg.getId(), msg.getRetryCount());
                    }
                }

            } catch (Exception e) {
                log.error("QueueWorker error: {}", e.getMessage(), e);
            }
        }

        log.info("QueueWorker for queue '{}' stopped", queue);
    }
}
