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
public class SyncMsgItem {

    @ApiModelProperty(value = "同步code")
    private String code;

    @ApiModelProperty(value = "同步键")
    private List<String> unique;

}
