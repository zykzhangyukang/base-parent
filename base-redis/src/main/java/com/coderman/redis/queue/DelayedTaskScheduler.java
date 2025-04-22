package com.coderman.redis.queue;

/**
 * @author ：zhangyukang
 * @date ：2025/04/22 10:47
 */

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DelayedTaskScheduler implements InitializingBean {

    @Resource
    private RedisService redisService;

    @Override
    public void afterPropertiesSet() {
        // Optional init logic
    }

    @Scheduled(fixedDelay = 1000)
    public void transferDelayedMessages() {
        Set<String> keys = this.redisService.keys("rqueue:delayed:*", RedisDbConstant.REDIS_DB_DEFAULT);
        long now = System.currentTimeMillis();
        if (keys != null) {
            for (String delayedKey : keys) {
                String queue = delayedKey.replace("rqueue:delayed:", "");
                Set<QueueMessage> queueMessages = this.redisService.zRangeByScore(delayedKey, QueueMessage.class, 0, now, RedisDbConstant.REDIS_DB_DEFAULT);
                if (queueMessages != null) {
                    for (QueueMessage msg : queueMessages) {
                        this.redisService.setListAppend("rqueue:queue:" + queue, msg, RedisDbConstant.REDIS_DB_DEFAULT);
                        this.redisService.zSetRemove(delayedKey, msg, RedisDbConstant.REDIS_DB_DEFAULT);
                    }
                }
            }
        }
    }
}
