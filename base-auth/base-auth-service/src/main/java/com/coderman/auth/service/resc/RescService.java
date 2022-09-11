package com.coderman.auth.service.resc;


import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.resc.RescVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author coderman
 * @Title: 资源服务
 * @Description: TOD
 * @date 2022/3/199:04
 */
public interface RescService {

    /**
     * 资源列表
     * @param currentPage
     * @param pageSize
     * @param queryVO
     * @return
     */
    ResultVO<PageVO<List<RescVO>>> page(Integer currentPage, Integer pageSize, RescVO queryVO);

    /**
     * 保存资源
     *
     * @param rescVO
     * @return
     */
    ResultVO<Void> save(RescVO rescVO);


    /**
     * 更新资源
     * @param rescVO
     * @return
     */
    ResultVO<Void> update(RescVO rescVO);


    /**
     * 删除资源
     * @param rescId
     * @return
     */
    ResultVO<Void> delete(Integer rescId);


    /**
     * 获取资源
     *
     * @param rescId
     * @return
     */
    ResultVO<RescVO> select(Integer rescId);


    /**
     * 根据关键词搜索资源
     *
     * @param keyword
     * @return
     */
    ResultVO<List<RescVO>> search(String keyword);


    /**
     * 根据用户名获取用资源信息
     *
     * @param username
     * @return
     */
    List<RescVO> selectRescListByUsername(String username);


    /**
     * 获取所有资源
     * @param project 所属项目
     * @return
     */
    ResultVO<Map<String, Set<Integer>>> getSystemAllRescMap(String project);
}
