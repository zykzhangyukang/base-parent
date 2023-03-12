package com.coderman.sync.service.message.impl;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.service.BaseService;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.message.MqMessageModel;
import com.coderman.sync.service.message.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl extends BaseService implements MessageService {


    @Override
    @LogError(value = "MQ消息发送记录列表")
    public ResultVO<PageVO<List<MqMessageModel>>> selectMessagePage(String srcProject, String destProject, String sendStatus, String dealStatus, Date startTime, Date endTime, String msgId, Integer currentPage, Integer pageSize) {

        if (null == currentPage) {
            currentPage = 1;
        }

        if (null == pageSize) {
            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        if (pageSize * currentPage > 3000) {

            return ResultUtil.getWarnPage(MqMessageModel.class, destProject + ":最大查询数量不能超过3000条,请精确条件在查询");
        }

        String dbname = SyncContext.getContext().getDbByProject(srcProject);

        if (StringUtils.isBlank(dbname)) {

            return ResultUtil.getWarnPage(MqMessageModel.class, "根据项目:" + srcProject + ",找不到对应的数据库");
        }

        String dbType = SyncContext.getContext().getDbType(dbname);

        if (SyncConstant.DB_TYPE_MONGO.equalsIgnoreCase(dbType)) {

            return this.selectMessageByMongo(srcProject, destProject, sendStatus, dealStatus, startTime, endTime, msgId, currentPage, pageSize, dbname);
        } else {

            return this.selectMessageBySql(srcProject, destProject, sendStatus, dealStatus, startTime, endTime, msgId, currentPage, pageSize, dbname);
        }
    }

    private ResultVO<PageVO<List<MqMessageModel>>> selectMessageBySql(String srcProject, String destProject, String sendStatus, String dealStatus, Date startTime, Date endTime, String msgId, Integer currentPage, Integer pageSize, String dbname) {

        StringBuilder builder = new StringBuilder();

        JdbcTemplate jdbcTemplate;

        try {
            jdbcTemplate = SpringContextUtil.getBean(dbname + "_template");
        } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {

            return ResultUtil.getWarnPage(MqMessageModel.class, "无此系统信息");
        }

        List<Object> paramList = new ArrayList<>();

        builder.append(" where src_project= ? ");
        paramList.add(srcProject);

        if (StringUtils.isNotBlank(destProject)) {

            builder.append(" and dest_project = ? ");
            paramList.add(destProject);
        }

        if (StringUtils.isNotBlank(sendStatus)) {

            builder.append(" and send_status  = ? ");
            paramList.add(sendStatus);
        }

        if (StringUtils.isNotBlank(dealStatus)) {

            builder.append(" and deal_status = ? ");
            paramList.add(dealStatus);
        }

        if (StringUtils.isNotBlank(msgId)) {

            builder.append(" and msg_id = ? ");
            paramList.add(msgId);
        }

        if (null != startTime) {

            builder.append(" and create_time >= ? ");
            paramList.add(startTime);
        }

        if (null != endTime) {

            builder.append(" and create_time < ? ");
            paramList.add(endTime);
        }

        Assert.notNull(jdbcTemplate, "jdbcTemplate is null");

        String sql = "select count(1) from pub_mq_message " + builder.toString();


        return null;
    }


    private ResultVO<PageVO<List<MqMessageModel>>> selectMessageByMongo(String srcProject, String destProject, String sendStatus, String dealStatus, Date startTime, Date endTime, String msgId, Integer currentPage, Integer pageSize, String dbname) {
        return null;
    }
}
