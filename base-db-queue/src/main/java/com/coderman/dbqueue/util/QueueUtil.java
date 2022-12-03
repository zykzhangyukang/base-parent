package com.coderman.dbqueue.util;

import com.coderman.dbqueue.dao.QueueDAO;
import com.coderman.dbqueue.model.QueueNode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author coderman
 * @Description: TOD
 * @date 2022/6/1814:44
 */
public class QueueUtil {

    private static QueueDAO queueDAO;

    private static QueueDAO getQueueDAO(){

        if (queueDAO == null) {
            queueDAO = QueueSpringContextUtil.getBean(QueueDAO.class);
        }
        return queueDAO;
    }

    /**
     * 队列中push
     * @param businessDataList
     * @param queueName
     * @return
     */
    public static int push(List<String> businessDataList, String queueName){

        if(CollectionUtils.isEmpty(businessDataList) || StringUtils.isEmpty(queueName)){

            return 0;
        }

        Date time = new Date();
        List<QueueNode> queueNodeList = businessDataList.stream().map(s -> new QueueNode(queueName, s)).collect(Collectors.toList());

        queueNodeList.forEach(node->node.setCreateTime(time));

        return getQueueDAO().insert(queueNodeList);
    }
}
