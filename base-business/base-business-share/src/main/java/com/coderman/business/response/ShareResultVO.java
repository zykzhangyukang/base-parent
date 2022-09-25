package com.coderman.business.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ShareResultVO<T extends ShareResultDetailVO> {

    @ApiModelProperty(value = "均摊结果明细集合")
    private List<T> shareResultDetailVOList;
}
