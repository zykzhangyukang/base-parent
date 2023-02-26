package com.coderman.sync.es;

import com.alibaba.fastjson.JSONObject;
import com.coderman.api.vo.PageVO;
import com.coderman.sync.result.ResultModel;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;

public interface EsService {

    /**
     * 批量查询同步记录
     *
     * @param resultModelList 同步记录list
     * @return
     */
    public boolean batchInsertSyncResult(List<ResultModel> resultModelList) throws IOException;

    /**
     * 修改同步结果为成功
     *
     * @param resultModel  消息同步结果
     * @param remark 备注信息
     */
    void updateSyncResultSuccess(ResultModel resultModel, String remark) throws IOException;


    /**
     * 同步记录搜索
     *
     * @param searchSourceBuilder 查询条件
     * @return
     */
    com.coderman.api.vo.ResultVO<PageVO<List<ResultModel>>> searchSyncResult(SearchSourceBuilder searchSourceBuilder) throws IOException;
}
