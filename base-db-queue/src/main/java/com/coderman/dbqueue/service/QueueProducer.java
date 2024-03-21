package com.coderman.dbqueue.service;

import com.coderman.dbqueue.dao.QueueDAO;
import com.coderman.dbqueue.util.QueueUtil;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产者
 * @author coderman
 */
public abstract class QueueProducer {


    @Resource
    private QueueDAO queueDAO;


    public void executeLongTimer(String param) {
        List<String> queueNodeList = this.produce(param);
        if (CollectionUtils.isEmpty(queueNodeList)) {
            return;
        }

        QueueUtil.push(queueNodeList, getQueueName());
    }

    /**
     * 生产者方法
     *
     * @param param
     * @return
     */
    protected abstract List<String> produce(String param);

    /**
     * 获取队列名称
     *
     * @return
     */
    protected abstract String getQueueName();
}