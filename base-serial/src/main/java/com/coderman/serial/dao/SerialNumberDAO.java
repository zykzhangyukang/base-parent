package com.coderman.serial.dao;

import com.coderman.mybatis.dao.BaseDAO;
import com.coderman.serial.model.SerialNumberExample;
import com.coderman.serial.model.SerialNumberModel;

import java.util.Map;

public interface SerialNumberDAO extends BaseDAO<SerialNumberModel, SerialNumberExample> {

    /**
     * 更新并且获取序列号
     *
     * @param paramMap
     */
    void getSerialNumber(Map<String, Object> paramMap);
}