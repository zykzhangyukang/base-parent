package com.coderman.auth.dao.resc;

import com.coderman.auth.model.resc.RescExample;
import com.coderman.auth.model.resc.RescModel;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RescDAO extends BaseDAO<RescModel, RescExample> {


    /**
     * 用户列表
     *
     * @param queryVO
     * @return
     */
    List<RescVO> page(@Param(value = "queryVO") RescVO queryVO);


    /**
     * 根据关键字搜索资源
     *
     * @param keyword
     * @return
     */
    List<RescVO> selectByKeyword(@Param(value = "keyword") String keyword);

    /**
     * 获取用户资源列表
     *
     * @param username
     * @return
     */
    List<RescVO> selectRescListByUsername(@Param(value = "username") String username);

}