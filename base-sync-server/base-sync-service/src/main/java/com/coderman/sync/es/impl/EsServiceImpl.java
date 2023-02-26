package com.coderman.sync.es.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.PageVO;
import com.coderman.service.anntation.LogError;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.es.EsService;
import com.coderman.sync.result.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EsServiceImpl implements EsService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    // 同步系统索引别名
    private static final String alias = "sync_alias";

    // 当前使用的索引名
    public String syncResultIndexName;


    @Override
    @LogError(value = "批量查询同步记录")
    public boolean batchInsertSyncResult(List<ResultModel> resultModelList) throws IOException {

        boolean result = false;

        BulkRequest bulkRequest = new BulkRequest();

        for (ResultModel resultModel : resultModelList) {
            IndexRequest indexRequest = new IndexRequest(this.syncResultIndexName).type("resultModel");
            indexRequest.source(JSON.toJSONString(resultModel), XContentType.JSON);
            indexRequest.id(resultModel.getUuid());
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (bulkResponse.hasFailures()) {

            log.error("批量插入同步记录到es错误:" + bulkResponse.buildFailureMessage());

        } else {

            result = true;

            List<String> uuidList = resultModelList.stream().map(ResultModel::getUuid).distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(uuidList)) {

                // 更新es同步状态
                JdbcTemplate jdbcTemplate = SpringContextUtil.getBean(JdbcTemplate.class);
                NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                Map<String, Object> params = new HashMap<>(uuidList.size());
                params.put("params", uuidList);
                namedParameterJdbcTemplate.update("update pub_sync_result set sync_To_es = 1 where uuid in (:params)", params);
            }

        }

        return result;
    }

    @Override
    @LogError(value = "修改同步结果为成功")
    public void updateSyncResultSuccess(ResultModel resultModel, String remark) throws IOException {

        UpdateByQueryRequest updateByQuery = new UpdateByQueryRequest(this.syncResultIndexName);

        updateByQuery.setRefresh(true);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("msgId", resultModel.getMsgId()))
                .should(QueryBuilders.termQuery("status", PlanConstant.RESULT_STATUS_FAIL));
        updateByQuery.setQuery(boolQueryBuilder);


        /**
         *   new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG,
         *                         "ctx._source.planUuid = '" + resultModel.getPlanUuid() + "';" +
         *                                 "ctx._source.planCode = '" + resultModel.getPlanCode() + "';" +
         *                                 "ctx._source.planName = '" + resultModel.getPlanName() + "';" +
         *                                 "ctx._source.srcProject = '" + resultModel.getSrcProject() + "';" +
         *                                 "ctx._source.destProject = '" + resultModel.getDestProject() + "';" +
         *                                 "ctx._source.syncContent = '" + resultModel.getSyncContent() + "';" +
         *                                 "ctx._source.status = 'success';" +
         *                                 "ctx._source.remark = '" + remark + "'", Collections.emptyMap())
         */

        updateByQuery.setBatchSize(100);
        updateByQuery.setSize(100);
        updateByQuery.setScript(
                new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG,
                        "ctx._source.status = 'success';" +
                                "ctx._source.remark = '" + remark + "'", Collections.emptyMap())
        );

        this.restHighLevelClient.updateByQuery(updateByQuery, RequestOptions.DEFAULT);
    }

    @Override
    @LogError(value = "同步记录搜索")
    public com.coderman.api.vo.ResultVO<PageVO<List<ResultModel>>> searchSyncResult(SearchSourceBuilder searchSourceBuilder) throws IOException {

        SearchRequest searchRequest = new SearchRequest(alias);

        searchRequest.source(searchSourceBuilder);
        searchRequest.types("resultModel");
        SearchResponse response = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        List<ResultModel> list = new ArrayList<>(30);

        for (SearchHit hit : hits) {

            list.add(JSON.parseObject(hit.getSourceAsString(), ResultModel.class));
        }

        PageVO<List<ResultModel>> pageVO = new PageVO<>(hits.getTotalHits(), list);
        return ResultUtil.getSuccessPage(ResultModel.class, pageVO);
    }


    @PostConstruct
    public void init() throws IOException {

        GetAliasesRequest getAliasesRequest = new GetAliasesRequest(alias);
        boolean existsAlias = this.restHighLevelClient.indices().existsAlias(getAliasesRequest, RequestOptions.DEFAULT);

        if (!existsAlias) {

            // 创建索引
            this.createResultModelIndex();

        } else {

            // 找出最新创建的一条索引为当前索引
            GetAliasesResponse response = this.restHighLevelClient.indices().getAlias(new GetAliasesRequest(EsServiceImpl.alias), RequestOptions.DEFAULT);

            List<Long> list = new ArrayList<>();

            for (Map.Entry<String, Set<AliasMetaData>> entry : response.getAliases().entrySet()) {

                String indexName = entry.getKey();

                if (indexName.matches("^sync_result_\\d{13}$")) {

                    list.add(Long.valueOf(indexName.replace("sync_result_", "")));
                }
            }

            list.sort((o1, o2) -> (int) (o2 - o1));

            this.syncResultIndexName = "sync_result_" + list.get(0);
            log.error("当前es工作索引为:{}", this.syncResultIndexName);
        }
    }

    /**
     * 创建索引
     */
    private void createResultModelIndex() throws IOException {

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("uuid")
                .field("type", "keyword")
                .endObject()
                .startObject("planUuid")
                .field("type", "keyword")
                .endObject()
                .startObject("planSrc")
                .field("type", "keyword")
                .endObject()
                .startObject("planCode")
                .field("type", "keyword")
                .endObject()
                .startObject("planName")
                .field("type", "text")
                .endObject()
                .startObject("mqId")
                .field("type", "keyword")
                .endObject()
                .startObject("msgId")
                .field("type", "keyword")
                .endObject()
                .startObject("msgContent")
                .field("type", "text")
                .endObject()
                .startObject("srcProject")
                .field("type", "keyword")
                .endObject()
                .startObject("destProject")
                .field("type", "keyword")
                .endObject()
                .startObject("syncContent")
                .field("type", "text")
                .endObject()
                .startObject("msgCreateTime")
                .field("type", "date")
                .field("format", "epoch_second")
                .endObject()
                .startObject("syncTime")
                .field("type", "date")
                .field("format", "epoch_second")
                .endObject()
                .startObject("status")
                .field("type", "keyword")
                .endObject()
                .startObject("errorMsg")
                .field("type", "text")
                .endObject()
                .startObject("repeatCount")
                .field("type", "integer")
                .endObject()
                .startObject("remark")
                .field("type", "text")
                .endObject()
                .endObject()
                .endObject();

        this.syncResultIndexName = "sync_result_" + System.currentTimeMillis();

        // 创建索引
        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        createIndexRequest.index(this.syncResultIndexName);
        createIndexRequest.alias(new Alias(EsServiceImpl.alias));
        createIndexRequest.mapping("resultModel", xContentBuilder);

        CreateIndexResponse indexResponse = this.restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        if (!indexResponse.isAcknowledged()) {

            throw new RuntimeException("创建索引失败");
        }
    }

}
