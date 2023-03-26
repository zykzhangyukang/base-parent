package com.coderman.sync.service.callback.impl;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.LogError;
import com.coderman.sync.callback.CallbackModel;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.service.callback.CallbackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CallbackServiceImpl implements CallbackService {


    @Override
    @LogError(value = "消息回调列表")
    public ResultVO<PageVO<List<CallbackModel>>> selectCallbackPage(String srcProject, String destProject, String status, Integer repeatCount, Date startTime, Date endTime, String planCode, String msgId, Integer currentPage, Integer pageSize) {

        if(currentPage == null){

            currentPage = 1;
        }

        if(pageSize == null){
            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        String dbname = SyncContext.getContext().getDbByProject(destProject);

        if(StringUtils.isBlank(dbname)){

            return ResultUtil.getWarnPage(CallbackModel.class,"无此系统信息");
        }

        String dbType = SyncContext.getContext().getDbType(dbname);

        if(StringUtils.isEmpty(dbType)){

            return ResultUtil.getWarnPage(CallbackModel.class,"无此系统数据源信息");
        }

        if(SyncConstant.DB_TYPE_MONGO.equals(dbType)){

            return this.selectCallbackPageByMongo(srcProject,destProject,status,repeatCount,startTime,endTime,planCode,msgId,currentPage,pageSize,dbname);
        }else {

            return this.selectCallbackPageBySql(srcProject,destProject,status,repeatCount,startTime,endTime,planCode,msgId,currentPage,pageSize,dbname);
        }
    }

    private ResultVO<PageVO<List<CallbackModel>>> selectCallbackPageByMongo(String srcProject, String destProject, String status, Integer repeatCount, Date startTime, Date endTime, String planCode, String msgId, Integer currentPage, Integer pageSize, String dbname) {
        return null;
    }

    private ResultVO<PageVO<List<CallbackModel>>> selectCallbackPageBySql(String srcProject, String destProject, String status, Integer repeatCount, Date startTime, Date endTime, String planCode, String msgId, Integer currentPage, Integer pageSize, String dbname) {
        return null;
    }
}
