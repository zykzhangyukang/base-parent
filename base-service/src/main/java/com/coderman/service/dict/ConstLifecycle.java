package com.coderman.service.dict;

import com.coderman.api.constant.RedisDbConstant;
import com.coderman.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Lazy(value = false)
public class ConstLifecycle implements SmartLifecycle {

    private final static Logger logger = LoggerFactory.getLogger(ConstLifecycle.class);


    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private RedisService redisService;


    @Override
    @SuppressWarnings("all")
    public void start() {

        if (this.initialized.compareAndSet(false, true)) {


            Object obj = redisService.getRedisTemplate().execute((RedisCallback) connection -> {

                connection.select(RedisDbConstant.REDIS_DB_DEFAULT);

                StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

                byte[] key = stringRedisSerializer.serialize("auth.const.all");
                byte[] domain = stringRedisSerializer.serialize(System.getProperty("domain"));
                byte[] value = genericJackson2JsonRedisSerializer.serialize(ConstService.getAllConstList());

                assert key != null;
                assert domain != null;
                assert value != null;
                return connection.hSet(key, domain, value);
            });

            logger.info("常量数据保存redis完成");

        }

    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop() {
        this.initialized.getAndSet(false);
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return this.initialized.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
