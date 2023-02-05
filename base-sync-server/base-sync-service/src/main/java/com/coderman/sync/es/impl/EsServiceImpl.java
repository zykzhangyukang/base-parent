package com.coderman.sync.es.impl;

import com.alibaba.fastjson.JSON;
import com.coderman.service.anntation.LogError;
import com.coderman.service.util.SpringContextUtil;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.es.EsService;
import com.coderman.sync.result.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.jdbc.core.JdbcTemplate;
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
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = this.restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (bulkResponse.hasFailures()) {

            log.error("批量插入同步记录到es错误:" + bulkResponse.buildFailureMessage());

        } else {

            result = true;

            // 更新同步状态
            List<String> uuidList = resultModelList.stream().map(ResultModel::getUuid).distinct().collect(Collectors.toList());

            JdbcTemplate jdbcTemplate = SpringContextUtil.getBean(JdbcTemplate.class);

            jdbcTemplate.update("update pub_sync_result set sync_To_es = true where uuid in (?)", uuidList);
        }

        return result;
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