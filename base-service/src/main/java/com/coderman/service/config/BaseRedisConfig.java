package com.coderman.service.config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author coderman
 */
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


}