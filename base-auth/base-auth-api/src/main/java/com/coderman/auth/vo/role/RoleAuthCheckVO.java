package com.coderman.auth.vo.role;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.model.func.FuncModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 角色分配功能检查
 * @Description: TOD
 * @date 2022/5/2115:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAuthCheckVO extends BaseModel {

    @ApiModelProperty(value = "本次新增")
    private List<FuncModel> insertList;

    @ApiModelProperty(value = "本次删除")
    private List<FuncModel> delList;
}
