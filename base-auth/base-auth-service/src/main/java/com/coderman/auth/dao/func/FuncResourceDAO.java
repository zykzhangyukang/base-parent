package com.coderman.auth.dao.func;

import com.coderman.auth.model.func.FuncResourceExample;
import com.coderman.auth.model.func.FuncResourceModel;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FuncResourceDAO extends BaseDAO<FuncResourceModel, FuncResourceExample> {


    /**
     * 批量插入功能资源关联
     * @param funcId
     * @param resourceIdList
     */
    void insertBatchByFuncId(@Param(value = "funcId") Integer funcId, @Param(value = "resourceIdList") List<Integer> resourceIdList);
}