package com.coderman.dbqueue.model;

/**
 * @author coderman
 * @Title: 数据node
 * @Description: TOD
 * @date 2022/6/1814:43
 */
import java.io.Serializable;
import java.util.Date;

public class QueueNode implements Serializable {

    /**
     * 消息id
     */
    private Integer nodeId;


    /**
     * 队列名称
     */
    private String queueName;


    /**
     * 业务数据
     */
    private String businessData;


    /**
     * 是否被消费
     */
    private Boolean isConsumed;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 消费时间
     */
    private Date consumeTime;

    public QueueNode() {
    }

    public QueueNode(String queueName, String businessData) {
        this.queueName = queueName;
        this.businessData = businessData;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getBusinessData() {
        return businessData;
    }

    public void setBusinessData(String businessData) {
        this.businessData = businessData;
    }

    public Boolean getConsumed() {
        return isConsumed;
    }

    public void setConsumed(Boolean consumed) {
        isConsumed = consumed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Date consumeTime) {
        this.consumeTime = consumeTime;
    }
}
