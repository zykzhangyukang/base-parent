package com.coderman.sync.es;

import com.coderman.sync.result.ResultModel;

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
}