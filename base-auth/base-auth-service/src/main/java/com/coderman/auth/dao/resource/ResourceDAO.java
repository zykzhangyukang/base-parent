package com.coderman.auth.dao.resource;

import com.coderman.auth.model.resource.ResourceExample;
import com.coderman.auth.model.resource.ResourceModel;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceDAO extends BaseDAO<ResourceModel, ResourceExample> {


    /**
     * 用户列表
     *
     * @param queryVO
     * @return
     */
    List<ResourceVO> page(@Param(value = "queryVO") ResourceVO queryVO);


    /**
     * 根据关键字搜索资源
     *
     * @param keyword
     * @return
     */
    List<ResourceVO> selectByKeyword(@Param(value = "keyword") String keyword);

    /**
     * 获取用户资源列表
     *
     * @param username
     * @return
     */
    List<ResourceVO> selectResourceListByUsername(@Param(value = "username") String username);
}