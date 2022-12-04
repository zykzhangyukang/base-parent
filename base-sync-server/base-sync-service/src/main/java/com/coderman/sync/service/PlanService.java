package com.coderman.sync.service;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.vo.PlanVO;

import java.util.List;

public interface PlanService {

    /**
     * 同步计划分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param queryVO     查询参数
     * @return
     */
    ResultVO<PageVO<List<PlanVO>>> page(Integer currentPage, Integer pageSize, PlanVO queryVO);


    /**
     * 查看同步内容
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<String> selectContent(String uuid);
}
