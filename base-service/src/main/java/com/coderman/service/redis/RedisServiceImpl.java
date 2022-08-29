package com.coderman.service.redis;

import com.coderman.service.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private byte[] serializeValue(Object obj) {
        return redisTemplate.getValueSerializer().serialize(obj);
    }

    @SuppressWarnings("unchecked")
    private byte[]  serializeHashValue(Object obj) {
        return this.redisTemplate.getHashValueSerializer().serialize(obj);
    }

    @SuppressWarnings("unchecked")
    private byte[]  serializeHashKey(Object obj) {
        return this.redisTemplate.getHashKeySerializer().serialize(obj);
    }


    private Object deserializeValue(byte[] bytes) {
        return this.redisTemplate.getValueSerializer().deserialize(bytes);
    }

    private Object deserializeKey(byte[] bytes) {
        return this.redisTemplate.getKeySerializer().deserialize(bytes);
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
    public void expire(String key, int seconds, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                redisConnection.expire(serializeKey(key), seconds);
                return null;
            }
        });

    }

    @Override
    public long incrBy(String key, long value, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                return redisConnection.incrBy(serializeKey(key), value);
            }
        });

        if (obj != null) {
            return (Long) obj;
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
            return (Long) obj;
        }

        return 0;
    }

    @Override
    public long hIncrBy(String key, String field, long value, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Long aLong = redisConnection.hIncrBy(serializeKey(key),serializeHashKey(field),value);
                return aLong;
            }
        });

        if (obj != null) {
            return (Long) obj;
        }

        return 0;

    }

    @Override
    public long hSize(String key, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Long aLong = redisConnection.hLen(serializeKey(key));
                return aLong;
            }
        });

        if (obj != null) {
            return (Long) obj;
        }

        return 0;

    }

    @Override
    public <T> long lSize(String key, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Long aLong = redisConnection.lLen(serializeKey(key));
                return aLong;
            }
        });

        if (obj != null) {
            return (Long) obj;
        }

        return 0;

    }

    @Override
    public long dbSize(int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Long aLong = redisConnection.dbSize();
                return aLong;
            }
        });

        if (obj != null) {
            return (Long) obj;
        }

        return 0;
    }

    @Override
    public long del(String key, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                return  redisConnection.del(serializeKey(key));
            }
        });

        if (obj != null) {
            return (Long) obj;
        }

        return 0;
    }

    @Override
    public long delKeys(List<String> keys, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);

                Long delNums = 0L;

                for (String key : keys) {
                    delNums += redisConnection.del(serializeKey(key));
                }


                return delNums;
            }
        });

        if (obj != null) {
            return (Long) obj;
        }

        return 0;

    }

    @Override
    public void delHash(String hashkey, String field, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                redisConnection.hDel(serializeKey(hashkey),serializeHashKey(field));
                return null;
            }
        });

    }

    @Override
    public void delHash(String key, List<String> filterField, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);

                for (String field : filterField) {
                    if(StringUtils.isNotBlank(field)){
                        redisConnection.hDel(serializeKey(key),serializeHashKey(field));
                    }
                }

                return null;
            }
        });

    }

    @Override
    public String getString(String key, int db) {

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                return deserializeValue(redisConnection.get(serializeKey(key)));
            }
        });

        if(obj!=null){
            return obj.toString();
        }

        return null;

    }


    @Override
    public String getStringBySameStrategy(String key, int db) {


        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                return deserializeKey(redisConnection.get(serializeKey(key)));
            }
        });

        if(obj!=null){
            return obj.toString();
        }

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


        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                redisConnection.select(db);
                Boolean set = redisConnection.set(serializeKey(key), serializeKey(string));
                return null;
            }
        });
    }

    @Override
    public <T> void setString(String key, String string, int seconds, int db) {
        Object execute = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                connection.select(db);
                Boolean set = connection.setEx(serializeKey(key), seconds,serializeValue(string));
                return set;
            }
        });
    }

    @Override
    public String getAndSetString(String key, String string, int seconds, int db) {

        Object execute = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                connection.select(db);
                Object obj = deserializeValue(connection.getSet(serializeKey(key), serializeValue(string)));
                connection.expire(serializeKey(key),seconds);
                return obj;
            }
        });

        if(execute!=null){
            return execute.toString();
        }

        return null;

    }

    @Override
    public <T> void setObject(String key, T obj, int db) {


        Object execute = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                connection.select(db);
                connection.set(serializeKey(key),serializeValue(obj));
                return null;
            }
        });

    }

    @Override
    public <T> void setObject(String key, T obj, int seconds, int db) {
        Object execute = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                connection.select(db);
                connection.setEx(serializeKey(key),seconds,serializeValue(obj));
                return null;
            }
        });
    }

    @Override
    public <T> T getObject(String key, Class<T> clas, int db) {

        Object execute = this.redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {

                connection.select(db);
                return deserializeValue(connection.get(serializeKey(key)));
            }
        });

        if(execute!=null){
            return (T) execute;
        }

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
                return connection.hSet(serializeKey(key), serializeHashKey(filed),serializeHashValue(obj));
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