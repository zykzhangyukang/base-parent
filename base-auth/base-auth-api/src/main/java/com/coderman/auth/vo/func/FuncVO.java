package com.coderman.auth.vo.func;

import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.vo.resc.RescVO;
import com.coderman.auth.vo.user.UserVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coderman
 * @Title: 功能
 * @Description: TOD
 * @date 2022/3/1915:35
 */
@Data
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
    private List<RescVO> rescVOList =  new ArrayList<>();


    /**
     * 资源id
     */
    private List<Integer> rescIdList =  new ArrayList<>();
}
