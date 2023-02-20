package com.coderman.sync.service.result;

import com.alibaba.fastjson.JSONObject;
import com.coderman.sync.vo.CompareVO;
import com.coderman.sync.vo.ResultVO;

import java.io.IOException;
import java.util.List;

public interface ResultService {

    /**
     * 同步记录搜索
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示条数
     * @param queryVO     参数条件
     * @return
     */
    JSONObject search(Integer currentPage, Integer pageSize, ResultVO queryVO) throws IOException;


    /**
     * 重新同步
     *
     * @param uuid uuid
     * @return
     */
    com.coderman.api.vo.ResultVO<Void> repeatSync(String uuid);


    /**
     * 标记成功
     *
     * @param uuid uuid
     * @return
     */
    com.coderman.api.vo.ResultVO<Void> signSuccess(String uuid) throws IOException;


    /**
     * 比对同步结果
     *
     * @param uuid
     * @return
     */
    com.coderman.api.vo.ResultVO<List<CompareVO>> selectTableData(String uuid, boolean convert) throws Throwable;
}
