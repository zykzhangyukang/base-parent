package com.coderman.limiter.config;

import com.coderman.limiter.aop.RateLimitAspect;
import com.coderman.limiter.resolver.KeyResolver;
import com.coderman.limiter.resolver.UriKeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

@Slf4j
@SuppressWarnings("all")
@Configuration
public class RateLimitConfig {

    public RateLimitConfig() {

        log.info("initialize redis rate limit...");
    }


    @Bean(name = "rateLimitRedisScript")
    public RedisScript<List<Long>> rateLimitRedisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(List.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/redis_rate_limiter.lua")));
        return redisScript;
    }

    @Bean
    public RateLimitAspect rateLimitAspect() {
        return new RateLimitAspect();
    }

    @Bean
    public KeyResolver keyResolver() {
        return new UriKeyResolver();
    }
}
