package com.coderman.redis.config;

import com.coderman.redis.annotaion.RedisChannelListener;
import com.coderman.redis.parser.RedisChannelListenerParser;
import com.coderman.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public abstract class BaseRedisConfig {


    public StringRedisTemplate createStringRedisTemplate(JedisConnectionFactory connectionFactory){

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

        stringRedisTemplate.setConnectionFactory(connectionFactory);
        stringRedisTemplate.setKeySerializer(stringRedisSerializer);
        stringRedisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        stringRedisTemplate.setHashKeySerializer(stringRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return stringRedisTemplate;
    }


    public JedisConnectionFactory createJedisConnectionFactory(RedisProperties properties, JedisPoolConfig jedisPoolConfig){

        RedisStandaloneConfiguration redisStandaloneConfiguration = createStandaloneConfig(properties);
        JedisClientConfiguration clientConfiguration = createJedisClientConf(properties,jedisPoolConfig);

        return new JedisConnectionFactory(redisStandaloneConfiguration,clientConfiguration);
    }

    private JedisClientConfiguration createJedisClientConf(RedisProperties properties, JedisPoolConfig jedisPoolConfig) {


        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();


        if(properties.getTimeout()!=null){

            Duration timeout = properties.getTimeout();
            builder.readTimeout(timeout).connectTimeout(timeout);
        }

        return builder.usePooling().poolConfig(jedisPoolConfig).build();
    }

    private RedisStandaloneConfiguration createStandaloneConfig(RedisProperties properties) {

        RedisStandaloneConfiguration config =  new RedisStandaloneConfiguration();

        config.setHostName(properties.getHost());
        config.setPort(properties.getPort());
        config.setPassword(RedisPassword.of(properties.getPassword()));
        config.setDatabase(properties.getDatabase());

        return config;
    }


    public RedisMessageListenerContainer redisMessageListenerContainer(@Autowired RedisService redisService, @Autowired RedisChannelListenerParser listenerParser) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(Objects.requireNonNull(redisService.getRedisTemplate().getConnectionFactory()));
        CopyOnWriteArraySet<RedisChannelListenerParser.RedisListenerMetaData> cacheList = listenerParser.getCacheList();

        for (RedisChannelListenerParser.RedisListenerMetaData metaData : cacheList) {

            RedisChannelListener[] listeners = metaData.getListeners();

            for (RedisChannelListener listener : listeners) {
                String channelName = listener.channelName();
                // Class<?> targetClazz = listener.clazz();
                MessageListenerAdapter adapter = new MessageListenerAdapter();
                Object bean = metaData.getBean();
                Method method = metaData.getMethod();
                String name = method.getName();
                adapter.setDelegate(bean);
                adapter.setDefaultListenerMethod(name);
                adapter.afterPropertiesSet();
                // 用于区分环境
                String relName = listener.envDiff() ? "DEV"+ "_" + channelName : channelName;
                container.addMessageListener(adapter, new ChannelTopic(relName));
                log.info("方法名{}订阅消息{}注册成功!!", method.toString(), relName);
            }
        }
        container.afterPropertiesSet();
        return container;
    }



}