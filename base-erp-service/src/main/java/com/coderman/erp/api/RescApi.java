package com.coderman.erp.api;

import com.coderman.api.vo.ResultVO;

import java.util.Map;
import java.util.Set;

/**
 * @author coderman
 */
public interface RescApi {


    /**
     * 获取所有资源
     * @return
     */
    public ResultVO<Map<String, Set<Integer>>> getSystemAllRescMap();


    /**获取所有资源
     *
     * @param project
     * @return
     */
    public ResultVO< Map<String, Set<Integer>>> getSystemAllRescMap(String project);
}
