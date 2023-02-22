package com.coderman.sync.service.result.impl;

import com.alibaba.fastjson.JSONObject;
import com.coderman.api.constant.CommonConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.service.anntation.LogError;
import com.coderman.sync.constant.PlanConstant;
import com.coderman.sync.constant.SyncConstant;
import com.coderman.sync.context.SyncContext;
import com.coderman.sync.es.EsService;
import com.coderman.sync.executor.AbstractExecutor;
import com.coderman.sync.plan.meta.*;
import com.coderman.sync.result.ResultModel;
import com.coderman.sync.service.result.ResultService;
import com.coderman.sync.sql.SelectBuilder;
import com.coderman.sync.sql.meta.SqlMeta;
import com.coderman.sync.task.SyncConvert;
import com.coderman.sync.task.SyncTask;
import com.coderman.sync.task.support.WriteBackTask;
import com.coderman.sync.vo.CompareVO;
import com.coderman.sync.vo.ResultVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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

        Date startTime = queryVO.getStartTime();
        Date endTime = queryVO.getEndTime();

        if (startTime != null && endTime != null) {

            queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").gte(startTime.getTime()).lt(endTime.getTime()));
        } else if (startTime != null) {

            queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").gte(startTime.getTime()));
        } else if (endTime != null) {

            queryBuilder.must(QueryBuilders.rangeQuery("msgCreateTime").lt(endTime.getTime()));
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

    @Override
    @LogError(value = "标记成功")
    public com.coderman.api.vo.ResultVO<Void> signSuccess(String uuid) throws IOException {

        if (StringUtils.isBlank(uuid)) {

            return ResultUtil.getWarn("uuid不能为空!");
        }

        ResultModel resultModel = this.jdbcTemplate.queryForObject("select status,msg_id,mq_id,msg_content,repeat_count " +
                        "from pub_sync_result where uuid=?",
                new BeanPropertyRowMapper<>(ResultModel.class), uuid);

        if (resultModel == null) {

            return ResultUtil.getWarn("同步记录不存在!");
        }

        if (!StringUtils.equals(resultModel.getStatus(), PlanConstant.RESULT_STATUS_FAIL)) {

            return ResultUtil.getWarn("请选择失败的记录进行标记!");
        }

        this.esService.updateSyncResultSuccess(resultModel, "手动标记成功");

        if (StringUtils.isNotBlank(resultModel.getMsgContent())) {

            SyncTask syncTask = SyncTask.build(resultModel.getMsgContent(), StringUtils.EMPTY, PlanConstant.MSG_SOURCE_HANDLE, 0);

            WriteBackTask writeBackTask = WriteBackTask.build(syncTask);
            writeBackTask.process();
        }

        return ResultUtil.getSuccess();
    }

    @Override
    public com.coderman.api.vo.ResultVO<List<CompareVO>> selectTableData(String msgContent, boolean convert) throws Throwable {

        // 构建消息对象
        MsgMeta msgMeta = MsgMeta.build(msgContent);

        PlanMeta planMeta = SyncContext.getContext().getPlanMeta(msgMeta.getPlanCode());

        // 构建执行器
        AbstractExecutor srcExecutor = this.buildExecutor(msgMeta, planMeta, "src");
        AbstractExecutor destExecutor = this.buildExecutor(msgMeta, planMeta, "dest");

        if (CollectionUtils.isEmpty(srcExecutor.getSqlList()) || CollectionUtils.isEmpty(destExecutor.getSqlList())) {

            return null;
        }

        // 查询源表数据
        List<SqlMeta> srcResultList = srcExecutor.execute();

        Map<String, CompareVO> srcResultMap = this.transformData(planMeta, srcResultList, convert, "src");

        // 查询目标表数据
        List<SqlMeta> destResultList = destExecutor.execute();

        Map<String, CompareVO> destResultMap = this.transformData(planMeta, destResultList, convert, "dest");


        // 封装结果集
        List<CompareVO> resultList =  new ArrayList<>();

        for (String key : srcResultMap.keySet()) {


            CompareVO compareVO = srcResultMap.get(key);
            if(destResultMap.containsKey(key)){

                compareVO.setDestResultList(destResultMap.get(key).getDestResultList());
            }else {

                int size = srcResultMap.get(key).getSrcResultList().size();

                Object[] tmpStr = new Object[size];

                Arrays.fill(tmpStr,"");
                compareVO.setDestResultList(Arrays.asList(tmpStr));
            }

            resultList.add(compareVO);
        }

        for (String key : destResultMap.keySet()) {

            if(srcResultMap.containsKey(key)){

                continue;
            }

            CompareVO compareVO = destResultMap.get(key);
            compareVO.setSrcResultList(new ArrayList<>());
            resultList.add(compareVO);
        }

        return ResultUtil.getSuccessList(CompareVO.class,resultList);
    }

    private Map<String, CompareVO> transformData(PlanMeta planMeta, List<SqlMeta> dataResultList, boolean convert, String flag) {

        boolean isSrc = StringUtils.equals("src", flag);

        Map<String, CompareVO> srcResultMap = new HashMap<>();

        for (SqlMeta sqlMeta : dataResultList) {

            if (CollectionUtils.isEmpty(sqlMeta.getResultList())) {

                continue;
            }

            // 封装表字段
            TableMeta tableMeta = SyncContext.getContext().getTableMeta(planMeta.getCode(), sqlMeta.getTableCode());

            for (Map<String, Object> resultMap : sqlMeta.getResultList()) {

                String key = sqlMeta.getTableCode() + PlanConstant.SPLIT_FLAG_POUND + resultMap.get(tableMeta.getUnique().getKey()) + PlanConstant.SPLIT_FLAG_POUND + resultMap.get(tableMeta.getRelate().getKey());
                List<Object> resultList = new ArrayList<>();

                boolean containUnique = false;

                List<String> srcColumnList = new ArrayList<>();
                List<String> destColumnList = new ArrayList<>();

                for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {

                    Object resultObj = resultMap.get(isSrc ? columnMeta.getSrc() : columnMeta.getDest());

                    if (resultObj instanceof Date) {

                        resultObj = DateUtils.round((Date) resultObj, Calendar.SECOND);
                    }

                    if (convert && MapUtils.isNotEmpty(columnMeta.getConverts())) {

//                        resultObj = SyncConvert.convert(resultObj,columnMeta.getType(),columnMeta.getConverts());
                    }

                    resultList.add(resultObj);
                    srcColumnList.add(columnMeta.getSrc());
                    destColumnList.add(columnMeta.getDest());

                    if (tableMeta.getUnique().getKey().equalsIgnoreCase(isSrc ? columnMeta.getSrc() : columnMeta.getDest())) {

                        containUnique = true;
                    }
                }

                if (!containUnique) {

                    srcColumnList.add(0, tableMeta.getUnique().getKey());
                    destColumnList.add(0, tableMeta.getUnique().getValue());

                    resultList.add(0, resultMap.get(tableMeta.getUnique().getKey()));
                }

                CompareVO compareVO = new CompareVO();
                compareVO.setSrcTable(tableMeta.getSrc());
                compareVO.setDestTable(tableMeta.getDest());
                compareVO.setSrcUnique(tableMeta.getUnique().getKey());
                compareVO.setDestUnique(tableMeta.getUnique().getValue());
                compareVO.setSrcColumnList(srcColumnList);
                compareVO.setDestColumnList(destColumnList);

                if (isSrc) {
                    compareVO.setSrcResultList(resultList);
                } else {
                    compareVO.setDestResultList(resultList);
                }

                srcResultMap.put(key, compareVO);


            }
        }

        return srcResultMap;
    }


    /**
     * 构建源表和目标表的执行器
     *
     * @param msgMeta
     * @param planMeta
     * @param flag
     * @return
     */
    private AbstractExecutor buildExecutor(MsgMeta msgMeta, PlanMeta planMeta, String flag) {

        String dbName;

        boolean isSrc = StringUtils.equals("src", flag);

        dbName = isSrc ? planMeta.getDbMeta().getSrcDb() : planMeta.getDbMeta().getDestDb();

        String dbType = SyncContext.getContext().getDbType(dbName);

        AbstractExecutor srcExecutor = AbstractExecutor.build(dbName);

        for (MsgTableMeta msgTableMeta : msgMeta.getTableMetaList()) {

            TableMeta tableMeta = SyncContext.getContext().getTableMeta(planMeta.getCode(), msgTableMeta.getCode());
            if (tableMeta == null || SyncConstant.OPERATE_TYPE_DELETE.equalsIgnoreCase(tableMeta.getType())) {

                continue;
            }

            SelectBuilder selectBuilder = SelectBuilder.create(dbType);

            selectBuilder.table(isSrc ? tableMeta.getSrc() : tableMeta.getDest());

            for (ColumnMeta columnMeta : tableMeta.getColumnMetas()) {

                selectBuilder.column(isSrc ? columnMeta.getSrc() : columnMeta.getDest());
            }

            selectBuilder.column(tableMeta.getRelate().getKey());
            selectBuilder.column(tableMeta.getUnique().getKey());
            selectBuilder.whereIn(tableMeta.getUnique().getKey(), msgTableMeta.getUniqueList().size());

            // 做一些数据转换
            String uniqueType = msgTableMeta.getUniqueType();
            if ("_id".equalsIgnoreCase(tableMeta.getUnique().getKey())) {

                uniqueType = SyncConvert.DATA_TYPE_OBJECTID;
            }

            List<Object> paramList = SyncConvert.convert(msgTableMeta.getUniqueList(), uniqueType);

            SqlMeta sqlMeta = new SqlMeta();
            sqlMeta.setSql(selectBuilder.sql());
            sqlMeta.setTableCode(tableMeta.getCode());
            sqlMeta.setSqlType(SyncConstant.OPERATE_TYPE_SELECT);
            sqlMeta.setParamList(SyncConvert.toArrayList(paramList));

            srcExecutor.sql(sqlMeta);
        }

        return srcExecutor;
    }
}
