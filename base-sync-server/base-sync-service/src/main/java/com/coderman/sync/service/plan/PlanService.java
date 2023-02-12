package com.coderman.sync.service.plan;

import com.alibaba.fastjson.JSONObject;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.vo.PlanVO;

import java.util.List;

public interface PlanService {

    /**
     * 同步计划分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    分页大小
     * @param sort        排序列
     * @param order       排序方式
     * @param queryVO     查询参数
     * @return
     */
    JSONObject page(Integer currentPage, Integer pageSize, String sort, String order, PlanVO queryVO);


    /**
     * 查看同步内容
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<String> selectContent(String uuid);


    /**
     * 同步计划更新
     *
     * @param planVO 参数对象
     * @return
     */
    ResultVO<Void> updatePlan(PlanVO planVO);


    /**
     * 启用/禁用 同步内容
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<Void> updateStatus(String uuid);


    /**
     * 同步计划新增
     *
     * @param planVO 参数对象
     * @return
     */
    ResultVO<Void> savePlan(PlanVO planVO);


    /**
     * 同步计划删除
     *
     * @param uuid uuid
     * @return
     */
    ResultVO<Void> deletePlan(String uuid);
}
