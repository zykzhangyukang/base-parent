package com.coderman.redis.queue;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 10:42
 */
@Component
@RequiredArgsConstructor
public class SimpleQueueManager {

    private final RedisService redisService;

    public void enqueue(String queue, String payload) {
        QueueMessage msg = createMessage(payload);
        this.redisService.setListAppend("rqueue:queue:" + queue, msg, RedisDbConstant.REDIS_DB_DEFAULT);
    }

    public void enqueueDelayed(String queue, String payload, Duration delay) {
        long score = System.currentTimeMillis() + delay.toMillis();
        QueueMessage msg = createMessage(payload);
        this.redisService.zSetAdd("rqueue:delayed:" + queue, msg, score, RedisDbConstant.REDIS_DB_DEFAULT);
    }

    private QueueMessage createMessage(String payload) {
        return QueueMessage.builder()
                .id(getMsgId())
                .payload(payload)
                .retryCount(0)
                .build();
    }

    private String getMsgId(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}