package com.coderman.business.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分摊明细
 */
@Data
public class ShareParamDetailVO implements Comparable<ShareParamDetailVO>{

    @ApiModelProperty(value = "分摊明细标识")
    private Integer shareDetailId;

    @ApiModelProperty(value = "参与分摊的分子")
    private BigDecimal shareNumerator;

    @ApiModelProperty(value = "允许分摊的最大值")
    private BigDecimal shareLimit;

    @Override
    public int compareTo(ShareParamDetailVO o) {

        if(this.shareNumerator == null){

            this.setShareNumerator(BigDecimal.ZERO);
        }

        if(o.shareNumerator == null){
            o.setShareNumerator(BigDecimal.ZERO);
        }

        return this.getShareNumerator().compareTo(o.getShareNumerator());
    }
}
