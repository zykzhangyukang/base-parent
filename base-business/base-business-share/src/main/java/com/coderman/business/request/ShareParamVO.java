package com.coderman.business.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author coderman
 */
@Data
public class ShareParamVO {

    @ApiModelProperty(value = "待分摊项")
    private BigDecimal waitShare;

    @ApiModelProperty(value = "待分摊分母")
    private BigDecimal waitShareDenominator;

    @ApiModelProperty(value = "要分摊总数的明细集合")
    private List<ShareParamDetailVO> shareParamDetailList;

    @ApiModelProperty(value = "待分摊项是否需要全部摊在明细上")
    private Boolean isShareAll;
}
