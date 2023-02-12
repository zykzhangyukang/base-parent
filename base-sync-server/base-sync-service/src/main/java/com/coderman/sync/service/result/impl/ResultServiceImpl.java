package com.coderman.sync.service.result.impl;

import com.alibaba.fastjson.JSONObject;
import com.coderman.api.constant.CommonConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.service.anntation.LogError;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.es.EsService;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.service.result.ResultService;
import com.coderman.sync.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class ResultServiceImpl implements ResultService {

    @Resource
    private EsService esService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    @LogError(value = "同步记录搜索")
    public JSONObject search(Integer currentPage, Integer pageSize, ResultVO queryVO) throws IOException {

        if (Objects.isNull(currentPage)) {
            currentPage = 1;
        }

        if (Objects.isNull(pageSize)) {

            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        if (pageSize * currentPage > 10000) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg", "最多查询1w行,请缩小范围查询!");
            jsonObject.put("rows", new ArrayList<>());
            jsonObject.put("total", 0);
            return jsonObject;
        }

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (queryVO.getStartTime() != null && queryVO.getEndTime() != null) {
            queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").gte(queryVO.getStartTime()).lt(queryVO.getEndTime()));
        } else if (queryVO.getStartTime() != null) {
            queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").gte(queryVO.getStartTime()));
        } else {
            queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").lt(queryVO.getEndTime()));
        }

        String keywords = queryVO.getKeywords();

        if (StringUtils.isNotBlank(keywords)) {


            BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
            shouldQuery.should(QueryBuilders.matchPhraseQuery("planName", keywords));
            shouldQuery.should(QueryBuilders.matchPhraseQuery("msgContent", keywords));
            shouldQuery.should(QueryBuilders.matchPhraseQuery("syncContent", keywords));

            queryBuilder.must(shouldQuery);

        }

        if (StringUtils.isNotBlank(queryVO.getPlanCode())) {

            queryBuilder.must(QueryBuilders.termQuery("planCode", queryVO.getPlanCode()));
        }

        if (StringUtils.isNotBlank(queryVO.getSyncStatus())) {

            queryBuilder.must(QueryBuilders.termQuery("status", queryVO.getSyncStatus()));
        }

        if (StringUtils.isNotBlank(queryVO.getMsgSrc())) {

            queryBuilder.must(QueryBuilders.termQuery("msgSrc", queryVO.getMsgSrc()));
        }


        if (StringUtils.isNotBlank(queryVO.getSrcProject())) {

            queryBuilder.must(QueryBuilders.termQuery("srcProject", queryVO.getSyncStatus()));
        }

        if (StringUtils.isNotBlank(queryVO.getDestProject())) {

            queryBuilder.must(QueryBuilders.termQuery("destProject", queryVO.getSyncStatus()));
        }

        if (queryVO.getRepeatCount() != null) {

            queryBuilder.must(QueryBuilders.rangeQuery("repeatCount").gte(queryVO.getRepeatCount()));
        }

        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .from((currentPage - 1) * pageSize)
                .size(pageSize)
                .sort("msgCreateTime", SortOrder.DESC)
                .sort("syncTime", SortOrder.DESC)
                .query(queryBuilder);

        return this.esService.searchSyncResult(searchSourceBuilder);
    }

    @Override
    @LogError(value = "重新同步")
    public com.coderman.api.vo.ResultVO<Void> repeatSync(String uuid) {

        if (StringUtils.isBlank(uuid)) {

            return ResultUtil.getWarn("uuid不能为空!");
        }

        ResultModel resultModel = this.jdbcTemplate.queryForObject("select status,mq_id,msg_content,repeat_count from pub_sync_result where uuid=?",
                new BeanPropertyRowMapper<>(ResultModel.class), uuid);

        if (resultModel == null) {

            return ResultUtil.getWarn("同步记录不存在!");
        }

        if (!StringUtils.equals(resultModel.getStatus(), PlanConstant.RESULT_STATUS_FAIL)) {

            return ResultUtil.getWarn("请选择失败的记录进行同步!");
        }

        String msgContent = resultModel.getMsgContent();

        SyncContext.getContext().syncData(msgContent, resultModel.getMqId(), PlanConstant.MSG_SOURCE_HANDLE, resultModel.getRepeatCount() + 1);

        return ResultUtil.getSuccess();
    }
}
