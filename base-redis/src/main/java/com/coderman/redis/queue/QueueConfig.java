package com.coderman.redis.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 16:53
 */
@Getter
@AllArgsConstructor
public class QueueConfig {
    private final String queue;
    private final int maxRetries;
    private final long retryDelay;
}
