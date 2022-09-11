package com.coderman.auth.dao.func;

import com.coderman.auth.model.func.FuncRescExample;
import com.coderman.auth.model.func.FuncRescModel;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FuncRescDAO extends BaseDAO<FuncRescModel, FuncRescExample> {


    /**
     * 批量插入功能资源关联
     * @param funcId
     * @param rescIdList
     */
    void insertBatchByFuncId(@Param(value = "funcId") Integer funcId, @Param(value = "rescIdList") List<Integer> rescIdList);
}