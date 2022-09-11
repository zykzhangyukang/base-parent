package com.coderman.auth.api.resc.impl;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.resc.RescService;
import com.coderman.erp.api.RescApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RescApiImpl implements RescApi {

    @Autowired
    private RescService rescService;

    @Override
    public ResultVO<Map<String, Set<Integer>>> getSystemAllRescMap() {
        return this.rescService.getSystemAllRescMap(null);
    }

    @Override
    public ResultVO<Map<String, Set<Integer>>> getSystemAllRescMap(String project) {
        return this.rescService.getSystemAllRescMap(project);
    }
}
