package com.coderman.service.redis;

import com.alibaba.fastjson.JSON;
import com.coderman.service.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("all")
public class RedisServiceImpl extends BaseService implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static String host;
    private static int port;


    @PostConstruct
    public void getHostAndPort() {

        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        assert jedisConnectionFactory != null;

        setHost(jedisConnectionFactory.getHostName());
        setPort(jedisConnectionFactory.getPort());

    }

    private void setPort(int port) {
        RedisServiceImpl.port = port;
    }

    private void setHost(String hostName) {
        RedisServiceImpl.host = hostName;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    @SuppressWarnings("unchecked")
    public byte[] serializeKey(String key) {
        return redisTemplate.getKeySerializer().serialize(key);
    }

    @SuppressWarnings("unchecked")
    private byte[] serializeValue(String string) {
        return redisTemplate.getValueSerializer().serialize(string);
    }

    @SuppressWarnings("unchecked")
    private byte[]  serializeHashValue(Object obj) {
        return this.redisTemplate.getHashValueSerializer().serialize(obj);
    }

    @SuppressWarnings("unchecked")
    private byte[]  serializeHashKey(Object obj) {
        return this.redisTemplate.getHashKeySerializer().serialize(obj);
    }


    @Override
    public boolean exists(String key, int db) {

        Object obj = redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.select(db);
            return redisConnection.exists(serializeKey(key));
        });

        if (obj != null) {
            return (boolean) obj;
        }

        return false;
    }


    @Override
    public boolean expire(String key, int seconds, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                return redisConnection.expire(serializeKey(key), seconds);
            }
        });

        if (obj != null) {
            return (boolean) obj;
        }

        return false;
    }

    @Override
    public long incrBy(String key, long value, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Long aLong = redisConnection.incrBy(serializeKey(key), value);
                return aLong;
            }
        });

        if (obj != null) {
            return (long) obj;
        }

        return 0;
    }

    @Override
    public long incr(String key, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Long aLong = redisConnection.incr(serializeKey(key));
                return aLong;
            }
        });

        if (obj != null) {
            return (long) obj;
        }

        return 0;
    }

    @Override
    public long hIncrBy(String key, String field, long value, int db) {
        return 0;
    }

    @Override
    public long hSize(String key, int db) {
        return 0;
    }

    @Override
    public <T> long lSize(String key, int db) {
        return 0;
    }

    @Override
    public long dbSize(int db) {
        return 0;
    }

    @Override
    public long del(String key, int db) {
        return 0;
    }

    @Override
    public long delKeys(List<String> keys, int db) {
        return 0;
    }

    @Override
    public void delHash(String hashkey, String key, int db) {

    }

    @Override
    public void delHash(String key, List<String> filterField, int db) {

    }

    @Override
    public String getString(String key, int db) {
        return null;
    }

    @Override
    public String getStringBySameStrategy(String key, int db) {
        return null;
    }

    @Override
    public <T> void setString(String key, String string, int db) {

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Boolean set = redisConnection.set(serializeKey(key), serializeValue(string));
                return null;
            }
        });
    }


    @Override
    public <T> void setStringBySameStrategy(String key, String string, int db) {

    }

    @Override
    public <T> void setString(String key, String string, int seconds, int db) {
        Object execute = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                connection.select(db);
                Boolean set = connection.set(serializeKey(key), serializeValue(string), Expiration.seconds(seconds), RedisStringCommands.SetOption.UPSERT);
                return set;
            }
        });
    }

    @Override
    public String getAndSetString(String key, String string, int seconds, int db) {
        return null;
    }

    @Override
    public <T> void setObject(String key, T obj, int db) {

    }

    @Override
    public <T> void setObject(String key, T obj, int seconds, int db) {

    }

    @Override
    public <T> T getObject(String key, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> long setList(String key, T obj, int db) {
        return 0;
    }

    @Override
    public <T> void setSet(String key, Set<T> set, int db) {

    }

    @Override
    public <T> void setListData(String key, int size, int db) {

    }

    @Override
    public <T> void delRListData(String key, int size, int db) {

    }

    @Override
    public <T> long setListAppend(String key, T obj, int db) {
        return 0;
    }

    @Override
    public <T> long setListAppendByCluster(String key, T obj, int db) {
        return 0;
    }

    @Override
    public <T> void setList(Map<String, T> map, int db) {

    }

    @Override
    public <T> List<T> getList(String key, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> Set<T> getSet(String key, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> List<T> getListData(String key, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> List<T> getList(List<String> filterKey, Class<T> cls, int db) {
        return null;
    }

    @Override
    public <T> Map<String, T> getMapOfList(List<String> filterKeys, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> void setHash(String key, String filed, T obj, int db) {
        this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                return connection.hSet(serializeHashKey(key), serializeKey(filed),serializeHashValue(obj));
            }
        });

    }


    @Override
    public <T> T getHash(String key, String filed, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> void setHash(String key, Map<String, T> map, int db) {

    }

    @Override
    public <T> void setHashByCluster(String key, Map<String, T> map, int db) {

    }

    @Override
    public <T> List<T> getHash(String key, List<String> filterField, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> Map<String, T> getMapOfHash(String key, List<String> filterField, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> Map<String, T> getMapOfHashByCluster(String key, List<String> filterField, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> List<T> getHashAll(String key, Class<T> clas, int db) {
        return null;
    }

    @Override
    public <T> Map<String, T> getMapOfHashAll(String key, Class<T> clas, int db) {
        return null;
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }

    @Override
    public String executeLuaScript(String luaScript, List<String> keys, Object... args) {
        return null;
    }

    @Override
    public Set<String> keys(String key, int db) {
        return null;
    }
}