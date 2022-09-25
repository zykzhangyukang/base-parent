package com.coderman.business.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareResultDetailVO {

    @ApiModelProperty(value = "分摊明细标识")
    private Integer shareDetailId;

    @ApiModelProperty(value = "本次分摊到的值")
    private BigDecimal shareValue;
}
