package com.coderman.redis.queue;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 10:41
 */
public interface QueueMessageHandler {
    void handle(QueueMessage message) throws Exception;
}
