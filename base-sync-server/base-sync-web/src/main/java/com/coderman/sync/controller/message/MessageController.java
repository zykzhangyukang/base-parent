package com.coderman.sync.controller.message;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.message.MqMessageModel;
import com.coderman.sync.service.message.MessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/page")
    public ResultVO<PageVO<List<MqMessageModel>>> selectMessagePage(String srcProject, String destProject, String sendStatus,String dealStatus, Date startTime, Date endTime,
                                                                    String msgId, Integer currentPage, Integer pageSize){

        return this.messageService.selectMessagePage(srcProject,destProject,sendStatus,dealStatus,startTime,endTime,msgId,currentPage,pageSize);
    }
}
