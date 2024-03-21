package com.coderman.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author coderman
 */
@Data
public class ExportPartVO<T> {

    @ApiModelProperty(value = "隐藏数据集合")
    private List<String> hiddenDataList;

    @ApiModelProperty(value = "规则数据集合")
    private List<T> ruleDataList;
}
