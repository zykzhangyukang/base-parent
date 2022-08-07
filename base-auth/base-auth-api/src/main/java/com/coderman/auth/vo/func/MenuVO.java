package com.coderman.auth.vo.func;

import com.coderman.api.model.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coderman
 * @Title: 菜单信息
 * @Description: TOD
 * @date 2022/5/312:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MenuVO")
public class MenuVO extends BaseModel {

    private Integer funcId;

    private Integer parentId;

    private String funcName;

    private String funcKey;

    private String funcIcon;

    List<MenuVO> children =  new ArrayList<>();
}
