package com.coderman.dbqueue.service;

/**
 * @author coderman
 * @Title: TODO
 * @Description: TOD
 * @date 2022/6/1814:46
 */

import com.coderman.dbqueue.dao.QueueDAO;
import com.coderman.dbqueue.model.QueueNode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消费者
 */
public abstract class QueueConsumer {


    @Autowired
    private QueueDAO queueDAO;



    public void executeLongTimer(String param){

        List<QueueNode> queueNodeList = popNodeList(this.getPopNum());
        if(CollectionUtils.isEmpty(queueNodeList)){
            return;
        }


        // 消费数据
        this.consume(queueNodeList,param);

        // 回写状态
        List<Integer> idList = queueNodeList.stream().map(QueueNode::getNodeId).collect(Collectors.toList());
        queueDAO.updateStatus(idList);

    }



    /**
     * pop一定数量的节点
     * @param popNum
     * @return
     */
    private List<QueueNode> popNodeList(Integer popNum) {
        return queueDAO.selectModelList(popNum,this.getQueueName());
    }

    /**
     * 获取队列名称
     *
     * @return
     */
    protected abstract String getQueueName();


    /**
     * 每次pop 出来的数量
     * @return
     */
    protected abstract Integer getPopNum();


    /**
     * 消费
     * @param queueNodeList
     * @param param
     */
    protected abstract void consume(List<QueueNode> queueNodeList, String param);

}
