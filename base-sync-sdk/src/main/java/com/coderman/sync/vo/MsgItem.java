package com.coderman.sync.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息对象
 */
public class MsgItem {

    @ApiModelProperty(value = "表别名")
    private String code;

    @ApiModelProperty(value = "关联键")
    private List<String> unique;


    public MsgItem(String code) {
        this.code = code;
        this.unique = new ArrayList<>();
    }


    public MsgItem() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getUnique() {
        return unique;
    }

    public void setUnique(List<String> unique) {
        this.unique = unique;
    }
}
