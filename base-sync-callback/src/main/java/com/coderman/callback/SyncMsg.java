package com.coderman.callback;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author coderman
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncMsg {

    @ApiModelProperty(value = "同步消息id")
    private String msgId;

    @ApiModelProperty(value = "同步计划编号")
    private String planCode;

    @ApiModelProperty(value = "表同步原数据")
    private List<SyncMsgItem> msgItemList;

    @ApiModelProperty(value = "同步消息")
    private String msg;
}
