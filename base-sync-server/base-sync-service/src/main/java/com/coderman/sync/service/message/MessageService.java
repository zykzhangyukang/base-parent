package com.coderman.sync.service.message;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.message.MqMessageModel;

import java.util.Date;
import java.util.List;

public interface MessageService {

    /**
     * 查询本地mq消息
     *
     * @param dealStatus 处理状态
     * @param srcProject 源系统
     * @param destProject 目标系统
     * @param sendStatus 发送状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param msgId 消息id
     * @param mid mq消息id
     * @param currentPage 分页
     * @param pageSize 每页条数
     * @return
     */
    ResultVO<PageVO<List<MqMessageModel>>> selectMessagePage(String srcProject, String destProject, String sendStatus, String dealStatus,Date startTime, Date endTime, String msgId,
                                                             String mid,Integer currentPage, Integer pageSize);
}
