package com.coderman.sync.jobhandler;

import com.coderman.api.vo.PageVO;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.es.EsService;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.service.result.ResultService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@JobHandler(value = "updateSuccessHandler")
@Component
@Slf4j
public class UpdateSuccessHandler extends IJobHandler {

    @Resource
    private EsService esService;

    @Resource
    private ResultService resultService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        XxlJobLogger.log("标记成功任务开始...");

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        int currentPage = 1;
        int pageSize = 2000;

        int begin = -30;
        int end = -10;

        String srcProject = "";

        try {

            if (StringUtils.isNotBlank(param) && StringUtils.contains(param, "#")) {
                begin = Integer.parseInt(param.split("#")[0]);
                end = Integer.parseInt(param.split("#")[1]);
            }

        } catch (Exception e) {
            log.error("update success error parse use default:{},begin:{},end:{}", e.getMessage(), begin, end);
        }

        Date now = new Date();
        Date startTime = DateUtils.addMinutes(now, begin);
        Date endTime = DateUtils.addMinutes(now, end);

        queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").gte(startTime.getTime()).lt(endTime.getTime()));
        queryBuilder.must(QueryBuilders.termQuery("status", PlanConstant.RESULT_STATUS_FAIL));
        if (StringUtils.isNotBlank(srcProject)) {
            queryBuilder.must(QueryBuilders.termQuery("srcProject", srcProject));
        }

        String keyword = "PRIMARY";
        keyword = new String(keyword.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
        shouldQuery.should(QueryBuilders.matchPhraseQuery("errorMsg", keyword));
        shouldQuery.minimumShouldMatch(1);
        queryBuilder.must(shouldQuery);

        PageVO<List<ResultModel>> pageVO = this.esService.searchSyncResult(SearchSourceBuilder.searchSource()
                .from((currentPage - 1) * pageSize)
                .size(pageSize)
                .query(queryBuilder)).getResult();

        int num = 0;

        if (null != pageVO && pageVO.getDataList() != null) {

            List<ResultModel> dataList = pageVO.getDataList();
            for (ResultModel resultModel : dataList) {

                num++;
                if (num % 20 == 0) {

                    TimeUnit.SECONDS.sleep(60);
                }

                XxlJobLogger.log("system-job标记成功任务,msgId..." + resultModel.getMsgId());
                resultService.signSuccess(resultModel.getUuid(),"JOB标记成功");
            }
        }

        XxlJobLogger.log("system-job标记成功任务,处理成功条数..." + num);
        return ReturnT.SUCCESS;
    }
}
