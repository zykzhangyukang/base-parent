package com.coderman.sync.controller.message;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.PageLimit;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.sync.message.MqMessageModel;
import com.coderman.sync.service.message.MessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/page")
    @PageLimit
    @ApiReturnIgnore
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"pageRow", "totalRow", "currPage", "totalPage", "dataList"}),
            @ApiReturnParam(name = "MqMessageModel",value = {"srcProject", "msgContent", "createTime", "destProject", "sendStatus", "dealStatus", "dealCount",
                    "sendTime", "ackTime", "mid"})
    })
    public ResultVO<PageVO<List<MqMessageModel>>> selectMessagePage(String srcProject, String destProject, String sendStatus,String dealStatus, Date startTime, Date endTime,
                                                                    String msgId, Integer currentPage, Integer pageSize){

        return this.messageService.selectMessagePage(srcProject,destProject,sendStatus,dealStatus,startTime,endTime,msgId,currentPage,pageSize);
    }
}
