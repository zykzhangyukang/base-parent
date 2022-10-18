package com.coderman.service.redis;

public interface RedisLockService {


    /**
     * 获取分布式锁
     *
     * @param lockName 分布式锁名称
     * @param expire   过期时间
     * @param timeout  获取分布式锁超时时间
     * @return
     */
    public boolean tryLock(String lockName, long expire, long timeout);


    /**
     * 释放分布式锁
     *
     * @param lockName 分布式锁名称
     */
    public void unlock(String lockName);


}
