package com.coderman.redis.service;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author zhangyukang
 */
public interface RedisService {

    /**
     * 判断指定数据库中的 key 是否存在。
     *
     * @param key 要检查的键名
     * @param db 数据库索引
     * @return 如果 key 存在于数据库中，返回 true；否则返回 false。
     */
    public boolean exists(String key, int db);

    /**
     * 设置指定键的过期时间。
     *
     * @param key     要设置过期时间的键名
     * @param seconds 过期的秒数
     * @param db      数据库索引
     */
    public void expire(String key, int seconds, int db);

    /**
     * 将指定键的值自增指定的量。
     *
     * @param key   要自增的键名
     * @param value 自增的值
     * @param db    数据库索引
     * @return 自增后的值
     */
    public long incrBy(String key, long value, int db);

    /**
     * 将指定键的值自增 1。
     *
     * @param key 要自增的键名
     * @param db  数据库索引
     * @return 自增后的值
     */
    public long incr(String key, int db);

    /**
     * 将指定哈希表的字段值自增指定的量。
     *
     * @param key    哈希表的键名
     * @param field  哈希表中的字段名
     * @param value  自增的值
     * @param db     数据库索引
     * @return 自增后的字段值
     */
    public long hIncrBy(String key, String field, long value, int db);

    /**
     * 获取指定哈希表的字段数量。
     *
     * @param key 哈希表的键名
     * @param db  数据库索引
     * @return 哈希表的字段数量
     */
    public long hSize(String key, int db);

    /**
     * 获取指定列表的长度。
     *
     * @param key 列表的键名
     * @param db  数据库索引
     * @param <T> 列表的元素类型
     * @return 列表的长度
     */
    public <T> long lSize(String key, int db);

    /**
     * 获取指定数据库的大小。
     *
     * @param db 数据库索引
     * @return 数据库的大小
     */
    public long dbSize(int db);

    /**
     * 删除指定键。
     *
     * @param key 要删除的键名
     * @param db  数据库索引
     * @return 被删除键的数量（0 或 1）
     */
    public long del(String key, int db);

    /**
     * 删除多个键。
     *
     * @param keys 要删除的键名列表
     * @param db   数据库索引
     * @return 被删除键的数量
     */
    public long delKeys(List<String> keys, int db);

    /**
     * 删除指定哈希表的字段。
     *
     * @param hashKey 哈希表的键名
     * @param key     哈希表中要删除的字段名
     * @param db      数据库索引
     */
    public void delHash(String hashKey, String key, int db);

    /**
     * 删除指定哈希表的多个字段。
     *
     * @param key         哈希表的键名
     * @param filterField 要删除的字段名列表
     * @param db          数据库索引
     */
    public void delHash(final String key, final List<String> filterField, final int db);

    /**
     * 获取指定键的字符串值。
     *
     * @param key 要获取值的键名
     * @param db  数据库索引
     * @return 键的字符串值
     */
    public String getString(String key, int db);

    /**
     * 根据相同策略获取指定键的字符串值。
     *
     * @param key 要获取值的键名
     * @param db  数据库索引
     * @return 键的字符串值
     */
    public String getStringBySameStrategy(String key, int db);

    /**
     * 设置指定键的字符串值。
     *
     * @param key    要设置值的键名
     * @param string 字符串值
     * @param db     数据库索引
     * @param <T>    字符串值的类型
     */
    public <T> void setString(String key, String string, int db);

    /**
     * 根据相同策略设置指定键的字符串值。
     *
     * @param key    要设置值的键名
     * @param string 字符串值
     * @param db     数据库索引
     * @param <T>    字符串值的类型
     */
    public <T> void setStringBySameStrategy(String key, String string, int db);


    /**
     * 设置指定键的字符串值，并设置过期时间（秒）。
     *
     * @param key     键名
     * @param string  字符串值
     * @param seconds 过期时间（秒）
     * @param db      数据库索引
     */
    <T> void setString(String key, String string, int seconds, int db);

    /**
     * 获取并设置指定键的字符串值，并设置过期时间（秒）。
     *
     * @param key     键名
     * @param string  字符串值
     * @param seconds 过期时间（秒）
     * @param db      数据库索引
     * @return 之前的字符串值，如果键不存在则返回 null
     */
    String getAndSetString(String key, String string, int seconds, int db);

    /**
     * 将对象存储到指定键中。
     *
     * @param key 键名
     * @param obj 要存储的对象
     * @param db  数据库索引
     */
    <T> void setObject(String key, T obj, int db);

    /**
     * 将对象存储到指定键中，并设置过期时间（秒）。
     *
     * @param key     键名
     * @param obj     要存储的对象
     * @param seconds 过期时间（秒）
     * @param db      数据库索引
     */
    <T> void setObject(String key, T obj, int seconds, int db);

    /**
     * 从指定键中获取对象。
     *
     * @param key  键名
     * @param clas 对象的类
     * @param db   数据库索引
     * @return 存储在键中的对象，如果键不存在则返回 null
     */
    <T> T getObject(String key, Class<T> clas, int db);


    /**
     * 将一个对象或对象列表存储到 Redis 集合中（无序集合）。
     *
     * @param key 键名
     * @param obj 要存储的对象或对象列表
     * @param db  数据库索引
     * @return 成功添加到集合中的元素数量
     */
    <T> long setList(String key, T obj, int db);

    /**
     * 将一个集合存储到 Redis 集合中（无序集合）。
     *
     * @param key 键名
     * @param set 要存储的集合
     * @param db  数据库索引
     */
    <T> void setSet(String key, Set<T> set, int db);


    /**
     * 将一个obj到 Redis 集合中（无序集合）。
     *
     * @param key 键名
     * @param obj 要存储的obj
     * @param db  数据库索引
     */
    <T> void addToSet(String key, T obj, int db);


    /**
     * 将一个集合存储到 Redis 集合中（无序集合）。
     *
     * @param key 键名
     * @param obj 要存储的obj
     * @param db  数据库索引
     * @return b
     */
    <T> boolean isSetMember(String key, T obj, int db);


    /**
     *
     * 将一个ob从Redis 集合中删除（无序集合）。
     * @param key 键名
     * @param obj 要存储的obj
     * @param db  数据库索引
     */
    <T> void  removeFromSet(String key, T obj, int db);

    /**
     * 将一个列表存储到 Redis 列表中（有序集合，从左侧插入）。
     *
     * @param key  键名
     * @param list 要存储的列表
     * @param db   数据库索引
     */
    <T> void setListData(String key, List<T> list, int db);

    /**
     * 删除 Redis 列表中指定数量的元素（从右侧删除）。
     *
     * @param key  键名
     * @param size 要删除的元素数量
     * @param db   数据库索引
     */
    <T>  void delRListData(String key, int size, int db);


    /**
     * 将一个对象或对象列表存储到 Redis 集合中（有序集合）。
     *
     * @param key     键名
     * @param obj     要存储的对象或对象列表
     * @param db      数据库索引
     * @return 成功添加到集合中的元素数量
     */
    <T> long setListAppend(String key, T obj, int db);

    /**
     * 将一组键值对存储到 Redis 中。
     *
     * @param map 键值对映射
     * @param db  数据库索引
     */
    <T> void setList(Map<String, T> map, int db);


    /**
     * 获取 Redis 集合中的元素列表。
     *
     * @param key  键名
     * @param clas 元素的类
     * @param db   数据库索引
     * @return Redis 集合中的元素列表，如果键不存在则返回 null
     */
    <T> List<T> getList(String key, Class<T> clas, int db);

    /**
     * 获取 Redis 集合中的元素集合。
     *
     * @param key  键名
     * @param clas 元素的类
     * @param db   数据库索引
     * @return Redis 集合中的元素集合，如果键不存在则返回 null
     */
    <T> Set<T> getSet(String key, Class<T> clas, int db);

    /**
     * 获取 Redis 列表中的元素列表。
     *
     * @param key  键名
     * @param clas 元素的类
     * @param db   数据库索引
     * @return Redis 列表中的元素列表，如果键不存在则返回空列表
     */
    <T> List<T> getListData(String key, Class<T> clas, int db);

    /**
     * 获取多个键的值并返回为列表。
     *
     * @param filterKey 要获取值的键名列表
     * @param cls       值的类
     * @param db        数据库索引
     * @return 多个键的值的列表
     */
    <T> List<T> getList(List<String> filterKey, Class<T> cls, int db);

    /**
     * 获取多个键的值并返回为键值对映射。
     *
     * @param filterKeys 要获取值的键名列表
     * @param clas       值的类
     * @param db         数据库索引
     * @return 多个键的值的键值对映射
     */
    <T> Map<String, T> getMapOfList(List<String> filterKeys, Class<T> clas, int db);

    /**
     * 将对象存储为哈希字段的值。
     *
     * @param key   哈希表的键名
     * @param field 哈希字段名
     * @param obj   要存储的对象
     * @param db    数据库索引
     */
    <T> void setHash(String key, String field, T obj, int db);

    /**
     * 获取哈希字段的值。
     *
     * @param key   哈希表的键名
     * @param field 哈希字段名
     * @param clas  字段的类
     * @param db    数据库索引
     * @return 哈希字段的值，如果字段不存在则返回 null
     */
    <T> T getHash(String key, String field, Class<T> clas, int db);


    /**
     * 将多个键值对存储为哈希字段的值。
     *
     * @param key 哈希表的键名
     * @param map 要存储的键值对映射
     * @param db  数据库索引
     */
    <T> void setHash(String key, Map<String, T> map, int db);

    /**
     * 获取多个哈希字段的值并返回为列表。
     *
     * @param key         哈希表的键名
     * @param filterField 要获取值的哈希字段列表
     * @param clas        字段的类
     * @param db          数据库索引
     * @return 多个哈希字段的值的列表
     */
    <T> List<T> getHash(String key, List<String> filterField, Class<T> clas, int db);

    /**
     * 获取多个哈希字段的值并返回为键值对映射。
     *
     * @param key         哈希表的键名
     * @param filterField 要获取值的哈希字段列表
     * @param clas        字段的类
     * @param db          数据库索引
     * @return 多个哈希字段的值的键值对映射
     */
    <T> Map<String, T> getMapOfHash(String key, List<String> filterField, Class<T> clas, int db);

    /**
     * 获取哈希表中所有字段的值并返回为列表。
     *
     * @param key  哈希表的键名
     * @param clas 字段的类
     * @param db   数据库索引
     * @return 哈希表中所有字段的值的列表
     */
    <T> List<T> getHashAll(String key, Class<T> clas, int db);

    /**
     * 获取哈希表中所有字段的值并返回为键值对映射。
     *
     * @param key  哈希表的键名
     * @param clas 字段的类
     * @param db   数据库索引
     * @return 哈希表中所有字段的值的键值对映射
     */
    <T> Map<String, T> getMapOfHashAll(String key, Class<T> clas, int db);


    /**
     * 获取用于执行 Redis 操作的 RedisTemplate 对象。
     *
     * @return RedisTemplate 对象
     */
    RedisTemplate getRedisTemplate();

    /**
     * 执行 Lua 脚本，并返回脚本执行结果。
     *
     * @param luaScript Lua 脚本字符串
     * @param keys      键名列表
     * @param args      参数列表
     * @return 脚本执行结果，如果执行失败则返回 null
     */
    List<Long> executeLuaScript(String luaScript, List<String> keys, Object... args);

    /**
     * 获取匹配指定模式的键集合。
     *
     * @param key 匹配模式
     * @param db  数据库索引
     * @return 匹配模式的键集合，如果没有匹配的键则返回空集合
     */
    Set<String> keys(String key, int db);


    /**
     * 向指定的 ZSet 集合中添加元素。
     *
     * @param key   ZSet 集合的键名
     * @param obj   要插入的元素，支持任意类型，将序列化存储在 ZSet 中
     * @param score 元素的分数，确定元素在 ZSet 中的排序
     * @param db    数据库索引，用于指定目标 Redis 数据库
     * @param <T>   要添加对象的类型，确保类型安全
     */
    <T> void zSetAdd(String key, T obj, double score, int db);

    /**
     * 向指定的 ZSet 集合中添加多个元素。
     *
     * @param key     ZSet 集合的键名
     * @param tuples  包含要插入元素及其分数的集合，支持任意类型的对象，元素会被序列化存储在 ZSet 中
     * @param db      数据库索引，用于指定目标 Redis 数据库
     * @param <T>     要添加对象的类型，确保类型安全
     */
    <T> void zAdd(String key, Set<RedisZSetCommands.Tuple> tuples, int db);


    /**
     * 从指定的 ZSet 集合中获取一定范围内的元素，按照分数从低到高的顺序返回。
     *
     * @param key        ZSet 集合的键名
     * @param beginIndex 起始索引（包含），表示获取范围的起点
     * @param endIndex   结束索引（包含），表示获取范围的终点。-1 表示获取到集合的末尾
     * @param db         数据库索引，用于指定目标 Redis 数据库
     * @param <T>        返回的元素类型
     * @return 返回指定范围内的元素集合，按分数升序排列
     */
    <T> Set<T> zRange(String key, Class<T> clazz, int beginIndex, int endIndex, int db);

    /**
     * 根据分数范围获取指定 ZSet 集合中的元素，返回符合分数区间 [min, max] 的元素集合，按分数升序排列。
     *
     * @param key   ZSet 集合的键名
     * @param clazz 返回的元素类型，用于反序列化每个元素
     * @param min   分数区间的最小值，包含此分数
     * @param max   分数区间的最大值，包含此分数
     * @param db    数据库索引，用于指定目标 Redis 数据库
     * @param <T>   返回的元素类型，确保类型安全
     * @return 返回符合分数区间的元素集合，按分数升序排列
     */
    <T> Set<T> zRangeByScore(String key, Class<T> clazz, double min, double max, int db);

    /**
     * 根据分数范围获取指定 ZSet 集合中的元素，返回符合分数区间 [min, max] 的元素集合，按分数降序排列。
     *
     * @param key   ZSet 集合的键名
     * @param clazz 返回的元素类型，用于反序列化每个元素
     * @param max   分数区间的最大值，包含此分数
     * @param min   分数区间的最小值，包含此分数
     * @param db    数据库索引，用于指定目标 Redis 数据库
     * @param <T>   返回的元素类型，确保类型安全
     * @return      返回符合分数区间的元素集合，按分数降序排列
     */
    <T> Set<T> zRevRangeByScore(String key, Class<T> clazz, double max, double min, int db);


    /**
     * 发布订阅消息
     *
     * @param topic 主题
     * @param msgObj 消息内容
     */
    void sendTopicMessage(String topic , Object msgObj);
}
