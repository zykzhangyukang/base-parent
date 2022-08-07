package com.coderman.auth.service.resource;


import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.resource.ResourceVO;

import java.util.List;

/**
 * @author coderman
 * @Title: 资源服务
 * @Description: TOD
 * @date 2022/3/199:04
 */
public interface ResourceService {

    /**
     * 资源列表
     * @param currentPage
     * @param pageSize
     * @param queryVO
     * @return
     */
    ResultVO<PageVO<List<ResourceVO>>> page(Integer currentPage, Integer pageSize, ResourceVO queryVO);

    /**
     * 保存资源
     *
     * @param resourceVO
     * @return
     */
    ResultVO<Void> save(ResourceVO resourceVO);


    /**
     * 更新资源
     * @param resourceVO
     * @return
     */
    ResultVO<Void> update(ResourceVO resourceVO);


    /**
     * 删除资源
     * @param resourceId
     * @return
     */
    ResultVO<Void> delete(Integer resourceId);


    /**
     * 获取资源
     *
     * @param resourceId
     * @return
     */
    ResultVO<ResourceVO> select(Integer resourceId);


    /**
     * 根据关键词搜索资源
     *
     * @param keyword
     * @return
     */
    ResultVO<List<ResourceVO>> search(String keyword);


    /**
     * 根据用户名获取用资源信息
     *
     * @param username
     * @return
     */
    List<ResourceVO> selectResourceListByUsername(String username);
}
