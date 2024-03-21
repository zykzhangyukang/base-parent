package com.coderman.sync.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息对象
 * @author coderman
 */
public class MsgItem {

    @ApiModelProperty(value = "表别名")
    private String code;

    @ApiModelProperty(value = "关联键")
    private List<String> unique;


    @ApiModelProperty(value = "必须受影响行数")
    private Integer mustAffectRows;


    public MsgItem(String code) {
        this.code = code;
        this.unique = new ArrayList<>();
    }


    public MsgItem(String code, Integer mustAffectRows) {
        this.code = code;
        this.mustAffectRows = mustAffectRows;
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

    public Integer getMustAffectRows() {
        return mustAffectRows;
    }

    public void setMustAffectRows(Integer mustAffectRows) {
        this.mustAffectRows = mustAffectRows;
    }
}
