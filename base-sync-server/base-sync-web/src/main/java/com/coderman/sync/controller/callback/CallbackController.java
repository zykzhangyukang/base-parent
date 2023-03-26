package com.coderman.sync.controller.callback;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.PageLimit;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.sync.callback.CallbackModel;
import com.coderman.sync.service.callback.CallbackService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/callback")
public class CallbackController {

    @Resource
    private CallbackService callbackService;

    @RequestMapping(value = "/page")
    @PageLimit(limitTotal = "100000")
    @ApiReturnIgnore
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"pageRow", "totalRow", "currPage", "totalPage", "dataList"}),
            @ApiReturnParam(name = "CallbackModel",value = {"srcProject", "msgContent", "createTime", "destProject", "sendStatus", "dealStatus", "dealCount",
                    "sendTime", "ackTime", "mid"})
    })
    public ResultVO<PageVO<List<CallbackModel>>> selectCallbackPage(String srcProject, String destProject, String status, Integer repeatCount, Date startTime, Date endTime,
                                                                   String planCode, String msgId, Integer currentPage, Integer pageSize){

        return this.callbackService.selectCallbackPage(srcProject,destProject,status,repeatCount,startTime,endTime,planCode,msgId,currentPage,pageSize);
    }

}
