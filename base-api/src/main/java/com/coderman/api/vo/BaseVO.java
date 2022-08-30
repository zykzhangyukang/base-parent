package com.coderman.api.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseVO {

    private static final long serialVersionUID = 4962543125725269908L;

    @Override
    public String toString() {
        try {
            return JSON.toJSONString(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.toString();
    }
}
