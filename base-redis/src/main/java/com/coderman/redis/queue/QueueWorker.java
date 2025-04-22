package com.coderman.redis.queue;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QueueWorker implements Runnable {

    private final String queue;
    private final QueueMessage msg;
    private final QueueMessageHandler handler;
    private final QueueConfig config;
    private final RedisService redisService;

    @Override
    public void run() {
        try {
            handler.handle(msg);
            log.debug("Processed message from [{}]: {}", queue, msg.getId());
        } catch (Exception ex) {
            log.warn("Handler error on message [{}]: {}", msg.getId(), ex.getMessage(), ex);
            if (msg.getRetryCount() < config.getMaxRetries()) {
                msg.setRetryCount(msg.getRetryCount() + 1);
                long retryTime = System.currentTimeMillis() + config.getRetryDelay();
                redisService.zSetAdd("rqueue:delayed:" + queue, msg, retryTime, RedisDbConstant.REDIS_DB_DEFAULT);
                log.info("Message [{}] scheduled for retry #{}, delay {}ms", msg.getId(), msg.getRetryCount(), config.getRetryDelay());
            } else {
                log.error("Message [{}] exceeded max retries. Dropping or moving to dead-letter queue", msg.getId());
            }
        }
    }
}
