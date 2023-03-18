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
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl extends BaseService implements MessageService {


    @Override
    @LogError(value = "MQ消息发送记录列表")
    public ResultVO<PageVO<List<MqMessageModel>>> selectMessagePage(String srcProject, String destProject, String sendStatus, String dealStatus, Date startTime, Date endTime, String msgId,String mid
            , Integer currentPage, Integer pageSize) {

        if (null == currentPage) {
            currentPage = 1;
        }

        if (null == pageSize) {
            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        if (StringUtils.isBlank(srcProject)) {

            return ResultUtil.getSuccessPage(MqMessageModel.class, new PageVO<>());
        }

        String dbname = SyncContext.getContext().getDbByProject(srcProject);

        if (StringUtils.isBlank(dbname)) {

            return ResultUtil.getWarnPage(MqMessageModel.class, "根据项目:" + srcProject + ",找不到对应的数据库");
        }

        String dbType = SyncContext.getContext().getDbType(dbname);

        if (SyncConstant.DB_TYPE_MONGO.equalsIgnoreCase(dbType)) {

            return this.selectMessageByMongo(dbType, srcProject, destProject, sendStatus, dealStatus, startTime, endTime, msgId,mid, currentPage, pageSize, dbname);
        } else {

            return this.selectMessageBySql(dbType, srcProject, destProject, sendStatus, dealStatus, startTime, endTime, msgId,mid, currentPage, pageSize, dbname);
        }
    }

    private ResultVO<PageVO<List<MqMessageModel>>> selectMessageBySql(String dbType, String srcProject, String destProject, String sendStatus, String dealStatus, Date startTime, Date endTime, String msgId,
                                                                      String mid,Integer currentPage, Integer pageSize, String dbname) {

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

            builder.append(" and uuid = ? ");
            paramList.add(msgId);
        }

        if (StringUtils.isNotBlank(mid)) {

            builder.append(" and mid = ? ");
            paramList.add(mid);
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

        if (SyncConstant.DB_TYPE_MSSQL.equalsIgnoreCase(dbType)) {

            sql = "select count(1) from pub_mq_message with (nolock) " + builder.toString();
        }

        Long count = jdbcTemplate.queryForObject(sql, paramList.toArray(), Long.class);

        List<MqMessageModel> list = new ArrayList<>();
        PageVO<List<MqMessageModel>> pageVO = new PageVO<>();

        pageVO.setTotalRow(count);
        pageVO.setCurrPage(currentPage);
        pageVO.setPageRow(pageSize);
        pageVO.setDataList(list);

        if (count != null && count > 0) {

            sql = this.buildSelectSql(count, dbType, currentPage * pageSize, builder.toString());

            if (SyncConstant.DB_TYPE_MSSQL.equalsIgnoreCase(dbType)) {

                sql += " ) A where rownumber > ? ";
                paramList.add((currentPage - 1) * pageSize);

            } else if (SyncConstant.DB_TYPE_ORACLE.equalsIgnoreCase(dbType)) {

                sql += " where rn>=? ";
                paramList.add((currentPage - 1) * pageSize);

            } else if (SyncConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)) {

                sql += " order by mq_message_id desc limit ?,? ";
                paramList.add((currentPage - 1) * pageSize);
                paramList.add(pageSize);

            }

            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, paramList.toArray());

            for (Map<String, Object> resultMap : resultList) {

                MqMessageModel mqMessageModel = new MqMessageModel();

                mqMessageModel.setMsgContent(resultMap.get("msg_content").toString());
                mqMessageModel.setSrcProject(resultMap.get("src_project").toString());
                mqMessageModel.setDestProject(resultMap.get("dest_project").toString());
                mqMessageModel.setMid(resultMap.get("mid") == null ? null : resultMap.get("mid").toString());
                mqMessageModel.setCreateTime(resultMap.get("create_time") == null ? null : this.formatTime(resultMap.get("create_time").toString()));
                mqMessageModel.setSendTime(resultMap.get("send_time") == null ? null : this.formatTime(resultMap.get("send_time").toString()));
                mqMessageModel.setAckTime(resultMap.get("ack_time") == null ? null : this.formatTime(resultMap.get("ack_time").toString()));
                mqMessageModel.setSendStatus(resultMap.get("send_status").toString());
                mqMessageModel.setDealStatus(resultMap.get("deal_status").toString());
                mqMessageModel.setDealCount(resultMap.get("deal_count") == null ? 1 : Integer.parseInt(resultMap.get("deal_count").toString()));

                list.add(mqMessageModel);
            }
        }

        return ResultUtil.getSuccessPage(MqMessageModel.class, pageVO);
    }

    private String buildSelectSql(Long count, String dbType, int endCount, String builderSql) {

        StringBuilder builder = new StringBuilder();

        builder.append("select ");

        if (dbType.equalsIgnoreCase(SyncConstant.DB_TYPE_MSSQL)) {

            builder.append(" top ").append(count).append(" * ");
            builder.append(" from (select row_number() over(order by mq_message_id desc)");
            builder.append(" as rownumber, msg_content,mid,src_project,dest_project,create_time,send_time,ack_time,send_status,deal_status,deal_count ");
            builder.append(" from pub_mq_message with(nolock) ");
            builder.append(builderSql);

        } else if (dbType.equalsIgnoreCase(SyncConstant.DB_TYPE_ORACLE)) {

            builder.append(" * ");
            builder.append(" from (select a1.*,rownum rn from (");
            builder.append("select msg_content,mid,src_project,dest_project,create_time,send_time,ack_time,send_status,deal_status,deal_count from pub_mq_message ");
            builder.append(builderSql);
            builder.append(" order by mq_message_id desc) a1 where rownum <=").append(endCount).append(") ");
        } else {

            builder.append(" msg_content,mid,src_project,dest_project,create_time,send_time,ack_time,send_status,deal_status,deal_count ");
            builder.append(" from pub_mq_message ");
            builder.append(builderSql);
        }

        return builder.toString();
    }

    private java.sql.Date getSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    private Date formatTime(String date) {
        if (date == null) {
            return null;
        }
        try {
            return DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss.SSS");
        } catch (ParseException e) {
            return null;
        }
    }

    private ResultVO<PageVO<List<MqMessageModel>>> selectMessageByMongo(String dbType, String srcProject, String destProject, String sendStatus, String dealStatus, Date startTime, Date endTime, String msgId,
                                                                        String mid,Integer currentPage, Integer pageSize, String dbname) {
        return null;
    }
}
