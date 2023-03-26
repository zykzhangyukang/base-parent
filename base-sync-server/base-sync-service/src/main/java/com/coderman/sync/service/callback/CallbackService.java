package com.coderman.sync.service.callback;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.callback.CallbackModel;

import java.util.Date;
import java.util.List;

public interface CallbackService {

    /**
     * 消息回调列表
     *
     * @param srcProject
     * @param destProject
     * @param status
     * @param repeatCount
     * @param startTime
     * @param endTime
     * @param planCode
     * @param msgId
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResultVO<PageVO<List<CallbackModel>>> selectCallbackPage(String srcProject, String destProject, String status, Integer repeatCount, Date startTime, Date endTime, String planCode, String msgId, Integer currentPage, Integer pageSize);
}
