package com.coderman.sync.config;

import com.coderman.service.config.BaseRedisConfig;
import com.coderman.sync.init.SyncPlanInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

/**
 * 配置同步计划广播消息
 */
@Configuration
@Slf4j
public class PlanRefreshConfig extends BaseRedisConfig {

    public static final String PLAN_REFRESH_KEY = "plan_refresh_key";

    @Resource
    private SyncPlanInitializer syncPlanInitializer;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory jedisConnectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(PLAN_REFRESH_KEY));

        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(){
        return new MessageListenerAdapter((MessageListener) (message, pattern) -> {

            try {

                log.info("刷新同步计划开始...");

                syncPlanInitializer.init();

                log.info("刷新同步计划结束...");

            }catch (Exception e){

                log.error("刷新同步计划失败...");
            }

        }, "onMessage");
    }


}
