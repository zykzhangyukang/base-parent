package com.coderman.log.alarm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 异常告警
 */
public class AlarmContext {

    private final static Logger logger  = LoggerFactory.getLogger(AlarmContext.class);

    /**
     * 存储上次告警的时间，Key:名称 Value:时间戳
     */
    public static final Map<String, AtomicLong> threadPoolExecutorAlarmTimeMap = new ConcurrentHashMap<>();


    /**
     * 队列存放告警任务
     */
    public static final LinkedBlockingDeque<AlarmMessage> DEQUE = new LinkedBlockingDeque<>(2000);

    /**
     * 告警线程池
     */
    public static ExecutorService EXECUTOR_SERVICE;

    static {

        // 告警消息超过2000直接丢弃掉
        EXECUTOR_SERVICE = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        // Deal with task
        doDeal();
    }

    private static void doDeal() {

        Thread thread = new Thread(() -> {

            while (true) {

                try {

                    AlarmMessage alarmMessage = DEQUE.take();

                    EXECUTOR_SERVICE.execute(() -> {

                        try {
                            sendAlarmMessage(alarmMessage);
                        } catch (Throwable ignored) {
                        }
                    });

                } catch (InterruptedException e) {

                    throw new RuntimeException(e);
                }

            }

        });


        thread.setName("ding告警线程");
        thread.setDaemon(true);
        thread.start();

        logger.info("ding告警线程启动....");

        thread.setUncaughtExceptionHandler((t, e) -> {

            if ("ding告警线程".equals(t.getName())) {

                doDeal();
            }
        });
    }

    /**
     * 加入告警队列
     *
     * @param message 告警消息
     */
    public static void add2Queue(AlarmMessage message) {
        DEQUE.add(message);
    }


    /**
     * 发送告警消息
     *
     * @param alarmMessage 告警消息
     */
    public static void sendAlarmMessage(AlarmMessage alarmMessage) {

        AtomicLong alarmTime = threadPoolExecutorAlarmTimeMap.get(alarmMessage.getAlarmName());
        if (alarmTime != null && (alarmTime.get() + alarmMessage.getAlarmTimeInterval() * 60 * 1000) > System.currentTimeMillis()) {
            return;
        }
        if (alarmMessage.getAlarmType() == AlarmTypeEnum.DING_TALK) {
            DingDingUtil.sendTextMessage(alarmMessage.getAccessToken(), alarmMessage.getAccessKeySecret(), alarmMessage.getMessage());
        }

        if (alarmMessage.getAlarmType() == AlarmTypeEnum.EXTERNAL_SYSTEM) {
            Map<String, String> data = new HashMap<>(2);
            data.put("alarmName", alarmMessage.getAlarmName());
            data.put("message", alarmMessage.getMessage());
            DingDingUtil.post(alarmMessage.getApiUrl(), JsonUtils.toJson(data));
        }

        threadPoolExecutorAlarmTimeMap.put(alarmMessage.getAlarmName(), new AtomicLong(System.currentTimeMillis()));

    }

}
