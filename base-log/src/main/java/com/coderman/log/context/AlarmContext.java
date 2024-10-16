package com.coderman.log.context;

import com.coderman.log.entity.AlarmMessage;
import com.coderman.log.entity.AlarmTypeEnum;
import com.coderman.log.utils.DingDingUtil;
import com.coderman.log.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 异常告警上下文管理类
 * 主要用于处理异常告警并发送到指定的告警渠道，例如钉钉或外部系统。
 * 优化了线程处理、异常捕获以及队列管理。
 *
 * @author coderman
 */
public class AlarmContext {

    private static final Logger logger = LoggerFactory.getLogger(AlarmContext.class);

    /**
     * 存储上次告警的时间，Key:告警名称, Value:时间戳
     */
    private static final Map<String, AtomicLong> ALARM_TIME_MAP = new ConcurrentHashMap<>();

    /**
     * 队列存放告警任务，最大容量2000，超过后会丢弃
     */
    private static final LinkedBlockingDeque<AlarmMessage> ALARM_QUEUE = new LinkedBlockingDeque<>(2000);

    /**
     * 告警线程池，最多允许处理2000个告警任务
     */
    private static final ExecutorService ALARM_EXECUTOR;

    static {
        ALARM_EXECUTOR = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        // 启动告警处理线程
        startAlarmProcessing();
    }

    /**
     * 启动告警处理线程，专门处理告警队列中的消息
     */
    private static void startAlarmProcessing() {
        Thread alarmThread = new Thread(() -> {
            while (true) {
                try {
                    // 从队列中获取告警消息进行处理
                    AlarmMessage alarmMessage = ALARM_QUEUE.take();
                    ALARM_EXECUTOR.execute(() -> sendAlarmMessage(alarmMessage));
                } catch (InterruptedException e) {
                    logger.error("告警处理线程被中断", e);
                    Thread.currentThread().interrupt();  // 重置中断状态
                } catch (Exception e) {
                    logger.error("告警处理时发生异常", e);
                }
            }
        });

        alarmThread.setName("DingDing-Alarm-Thread");
        alarmThread.setDaemon(true);
        alarmThread.setUncaughtExceptionHandler((t, e) -> {
            logger.error("线程 [{}] 出现未捕获异常，重新启动线程", t.getName(), e);
            startAlarmProcessing();  // 重新启动告警处理线程
        });
        alarmThread.start();

        logger.info("告警处理线程启动成功...");
    }

    /**
     * 将告警消息加入队列进行异步处理
     *
     * @param message 告警消息
     */
    public static void addToQueue(AlarmMessage message) {
        if (!ALARM_QUEUE.offer(message)) {
            logger.warn("告警队列已满，丢弃告警: {}", message.getAlarmName());
        }
    }

    /**
     * 发送告警消息，根据告警类型分别处理
     *
     * @param alarmMessage 告警消息
     */
    private static void sendAlarmMessage(AlarmMessage alarmMessage) {
        AtomicLong lastAlarmTime = ALARM_TIME_MAP.get(alarmMessage.getAlarmName());

        // 判断告警时间间隔，避免频繁告警
        if (lastAlarmTime != null && (lastAlarmTime.get() + alarmMessage.getAlarmTimeInterval() * 60 * 1000) > System.currentTimeMillis()) {
            logger.debug("告警 [{}] 已在间隔时间内，跳过本次告警", alarmMessage.getAlarmName());
            return;
        }

        try {
            // 根据告警类型发送不同类型的告警
            if (alarmMessage.getAlarmType() == AlarmTypeEnum.DING_TALK) {
                DingDingUtil.sendTextMessage(alarmMessage.getAccessToken(), alarmMessage.getAccessKeySecret(), alarmMessage.getMessage());
            } else if (alarmMessage.getAlarmType() == AlarmTypeEnum.EXTERNAL_SYSTEM) {
                Map<String, String> data = new HashMap<>(2);
                data.put("alarmName", alarmMessage.getAlarmName());
                data.put("message", alarmMessage.getMessage());
                DingDingUtil.post(alarmMessage.getApiUrl(), JsonUtils.toJson(data));
            }

            // 更新最后一次告警时间
            ALARM_TIME_MAP.put(alarmMessage.getAlarmName(), new AtomicLong(System.currentTimeMillis()));

        } catch (Exception e) {
            logger.error("发送告警消息 [{}] 失败", alarmMessage.getAlarmName(), e);
        }
    }
}
