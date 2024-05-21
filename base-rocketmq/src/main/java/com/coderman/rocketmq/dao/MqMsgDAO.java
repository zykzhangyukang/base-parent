package com.coderman.rocketmq.dao;

import com.coderman.rocketmq.model.MqMsgExample;
import com.coderman.rocketmq.model.MqMsgModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coderman
 */
public interface MqMsgDAO  {

    /**
     * 插入
     *
     * @param record
     * @return
     */
    int insert(MqMsgModel record);



    /**
     * 根据条件查询
     *
     * @param example
     * @return
     */
    List<MqMsgModel> selectByExample(MqMsgExample example);


    /**
     * 动态更新
     *
     * @param record
     * @param example
     * @return
     */
    int updateByExampleSelective(@Param("record") MqMsgModel record, @Param("example") MqMsgExample example);

    /**
     * 根据主键动态更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(MqMsgModel record);


}