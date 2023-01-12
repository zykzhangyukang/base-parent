package com.coderman.sync.task.base;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseTask implements Delayed {

    private long delayTime;

    private final AtomicInteger retry =  new AtomicInteger(0);

    public void setDelayTime(long delayTime) {
        this.delayTime = System.nanoTime() + TimeUnit.SECONDS.toNanos(delayTime);
    }


    public Integer getRetryTimes() {
        return retry.getAndIncrement();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime  - System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.SECONDS) - delayed.getDelay(TimeUnit.SECONDS));
    }
}
