package com.coderman.redis.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 10:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueMessage implements Serializable {
    private String id;
    private String payload;
    private int retryCount;
}
