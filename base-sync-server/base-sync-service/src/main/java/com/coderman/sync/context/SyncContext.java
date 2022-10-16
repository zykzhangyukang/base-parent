package com.coderman.sync.context;

import com.alibaba.fastjson.JSON;
import com.coderman.service.util.SpringContextUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Configuration
@Lazy(value = false)
@DependsOn(value = "springContextUtil")
public class SyncContext {


    private final static Logger logger  = LoggerFactory.getLogger(SyncContext.class);


    private static SyncContext syncContext;

    // 数据库名称与类型之间的关系
    private Map<String,String> dbTypeMap = new ConcurrentHashMap<String,String>();


    // 是否锁定同步任务,默认锁定
    private boolean lockSyncTask = true;

    // 正在进行中的同步任务数量
    private AtomicInteger syncTaskCount = new AtomicInteger(0);

    /**
     * 消息处理结果
     *
     * @return
     */
    public static final String SYNC_END = "end";
    public static final String SYNC_RETRY = "retry";

    public static SyncContext getContext(){
        return syncContext;
    }


    /**
     * 设置项目与数据库连接之间的关系
     */
    public void relateDbType(String dbname,String type){
        this.dbTypeMap.put(dbname,type);
    }


    @PostConstruct
    public void init(){
        SyncContext.syncContext = SpringContextUtil.getBean(SyncContext.class);
    }


    /**
     * 同步数据
     * @param msg
     * @param msgId
     * @param msgRocketMq
     * @param reconsumeTimes
     * @return
     */
    public String syncData(String msg, String msgId, String msgRocketMq, int reconsumeTimes) {

        String result = SyncContext.SYNC_END;

        // 标记开始同步任务
        this.startSync();

        try {

            // 构建同步任务
            // todo result =
            logger.info("同步任务:{}", JSON.toJSONString(msg));

        }catch (Exception e){

            logger.error("同步数据错误:{}",e.getMessage(),e);

        }finally {

            this.endSync();
        }

        return result;
    }

    /**
     * 结束同步任务
     *
     * @return
     */
    private int endSync() {

        return this.syncTaskCount.decrementAndGet();
    }


    /**
     * 开始同步
     */
    private int startSync() {

        int count = 0;

        // 被锁定,暂停后继续执行
        while (this.lockSyncTask){

            logger.error("暂停同步任务,等待同步计划刷新");

            if(count >20){

                logger.error("同步任务锁定超时,自动结束");
            }

            count ++;

            try {

                Thread.sleep(500);

            } catch (InterruptedException e) {

                logger.error("暂停同步任务失败:{}",e.getMessage(),e);
            }
        }

        return this.syncTaskCount.incrementAndGet();
    }

}
