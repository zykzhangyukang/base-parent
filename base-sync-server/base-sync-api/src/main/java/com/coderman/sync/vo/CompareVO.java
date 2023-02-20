package com.coderman.sync.vo;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompareVO extends BaseModel {

    @ApiModelProperty(value = "源表")
    private String srcTable;

    @ApiModelProperty(value = "目标表")
    private String destTable;

    @ApiModelProperty(value = "源表主键")
    private String srcUnique;

    @ApiModelProperty(value = "目标表主键")
    private String destUnique;

    @ApiModelProperty(value = "源表字段")
    private List<String> srcColumnList;

    @ApiModelProperty(value = "目标表字段")
    private List<String> destColumnList;

    @ApiModelProperty(value = "源表字段值")
    private List<Object> srcResultList;

    @ApiModelProperty(value = "目标表字段值")
    private List<Object> destResultList;



}
