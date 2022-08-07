package com.coderman.auth.vo.func;

import com.coderman.auth.model.func.FuncModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author coderman
 * @Title: 查询对象
 * @Description: TOD
 * @date 2022/5/212:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FuncQueryVO extends FuncModel {

    @ApiModelProperty(value = "资源url")
    private String resourceUrl;
}
