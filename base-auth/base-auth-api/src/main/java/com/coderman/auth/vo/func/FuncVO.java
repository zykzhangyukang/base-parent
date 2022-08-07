package com.coderman.auth.vo.func;

import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author coderman
 * @Title: 功能
 * @Description: TOD
 * @date 2022/3/1915:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FuncVO extends FuncModel {

    /**
     * 父级功能名称
     */
    private String parentFuncName;

    /**
     * 树形结构
     */
    private List<FuncTreeVO> funcTreeVOList;


    /**
     * 非树形结构
     */
    private List<FuncTreeVO> funcVOList;


    /**
     * 部门用户信息
     */
    private List<UserVO> userVOList;


    /**
     * 资源信息
     */
    private List<ResourceVO> resourceVOList =  new ArrayList<>();


    /**
     * 资源id
     */
    private List<Integer> resourceIdList =  new ArrayList<>();
}
