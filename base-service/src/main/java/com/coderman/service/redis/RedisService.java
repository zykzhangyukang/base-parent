package com.coderman.service.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface RedisService {

    /**
     * 判断key是否存在
     *
     * @param key key
     * @param db  数据库索引
     */
    public boolean exists(String key, int db);


    /**
     * 设置key过期
     * @param key key
     * @param seconds 过期秒数
     * @param db 数据库索引
     */
    public void expire(String key, int seconds, int db);

    /**
     * key自增
     * @param key key
     * @param value 自增值
     * @param db 数据库索引
     */
    public long incrBy(String key, long value, int db);

    /**
     * key自增1
     * @param key key
     * @param db 数据库索引
     */
    public long incr(String key, int db);

    public long hIncrBy(String key, String field, long value, int db);

    public long hSize(String key, int db);

    public <T> long lSize(String key, int db);

    public long dbSize(int db);

    public long del(String key, int db);

    public long delKeys(List<String> keys, int db);

    public void delHash(String hashkey, String key, int db);

    public void delHash(final String key, final List<String> filterField, final int db);

    public String getString(String key, int db);

    public String getStringBySameStrategy(String key, int db);

    public <T> void setString(String key, String string, int db);

    public <T> void setStringBySameStrategy(String key, String string, int db);

    public <T> void setString(String key, String string, int seconds, int db);

    public String getAndSetString(String key, String string, int seconds, int db);

    public <T> void setObject(String key, T obj, int db);

    public <T> void setObject(String key, T obj, int seconds, int db);

    public <T> T getObject(String key, Class<T> clas, int db);

    public <T> long setList(String key, T obj, int db);

    public <T> void setSet(String key, Set<T> set, int db);

    public <T> void setListData(String key, List<T> list, int db);

    public <T> void delRListData(String key, int size, int db);

    public <T> long setListAppend(String key, T obj, int db);


    public <T> void setList(Map<String, T> map, int db);

    public <T> List<T> getList(String key, Class<T> clas, int db);

    public <T> Set<T> getSet(String key, Class<T> clas, int db);

    public <T> List<T> getListData(String key, Class<T> clas, int db);

    public <T> List<T> getList(List<String> filterKey, Class<T> cls, int db);

    public <T> Map<String, T> getMapOfList(List<String> filterKeys, Class<T> clas, int db);

    public <T> void setHash(String key, String filed, T obj, int db);

    public <T> T getHash(String key, String filed, Class<T> clas, int db);

    public <T> void setHash(String key, Map<String, T> map, int db);



    public <T> List<T> getHash(String key, List<String> filterField, Class<T> clas, int db);

    public <T> Map<String, T> getMapOfHash(String key, List<String> filterField, Class<T> clas, int db);


    public <T> List<T> getHashAll(String key, Class<T> clas, int db);

    public <T> Map<String, T> getMapOfHashAll(String key, Class<T> clas, int db);

    public RedisTemplate getRedisTemplate();

    public String executeLuaScript(String luaScript, List<String> keys, Object... args);

    public Set<String> keys(String key, int db);
}
