package com.coderman.redis.config;

import com.coderman.redis.annotaion.RedisChannelListener;
import com.coderman.redis.parser.CommonRedisSerializer;
import com.coderman.redis.parser.RedisChannelListenerParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Stream;

/**
 * @author zhangyukang
 */
@Slf4j
public abstract class BaseRedisConfig implements EnvironmentAware {

    private Environment environment;

    @Resource
    private RedisChannelListenerParser listenerParser;


    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }


    /**
     * 获取当前的环境
     *
     * @return
     */
    public String getCurrentEnv() {
        String[] profiles = environment.getActiveProfiles();
        return Stream.of(profiles).findFirst().orElse(null);
    }


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


    public RedisTemplate createRedisTemplate(JedisConnectionFactory connectionFactory) {

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        RedisTemplate redisTemplate = new RedisTemplate();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
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


    public RedisMessageListenerContainer createMessageListenerContainer(JedisConnectionFactory jedisConnectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        CopyOnWriteArraySet<RedisChannelListenerParser.RedisListenerMetaData> cacheList = listenerParser.getCacheList();

        for (RedisChannelListenerParser.RedisListenerMetaData metaData : cacheList) {

            RedisChannelListener[] listeners = metaData.getListeners();

            for (RedisChannelListener listener : listeners) {

                String channelName = listener.channelName();
                Class<?> clazz = listener.clazz();
                MessageListenerAdapter adapter = new MessageListenerAdapter();
                Object bean = metaData.getBean();
                Method method = metaData.getMethod();
                String name = method.getName();
                adapter.setDelegate(bean);
                adapter.setDefaultListenerMethod(name);
                adapter.afterPropertiesSet();
                adapter.setSerializer(new CommonRedisSerializer<>(clazz));
                container.addMessageListener(adapter, new ChannelTopic(channelName));

                log.info("方法名{} 订阅消息{} 注册成功!!", method, channelName);
            }
        }
        container.afterPropertiesSet();
        return container;
    }



}