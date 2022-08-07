package com.coderman.auth.service.role;


import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.role.RoleAssignVO;
import com.coderman.auth.vo.role.RoleAuthCheckVO;
import com.coderman.auth.vo.role.RoleAuthInitVO;
import com.coderman.auth.vo.role.RoleVO;

import java.util.List;

/**
 * @author coderman
 * @date 2022/2/2711:41
 */
public interface RoleService {

    /**
     * 角色列表
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    ResultVO<PageVO<List<RoleVO>>> page(Integer currentPage, Integer pageSize, RoleVO queryVO);

    /**
     * 角色新增
     *
     * @param roleVO
     * @return
     */
    ResultVO<Void> save(RoleVO roleVO);


    /**
     * 角色删除
     *
     * @param roleId
     * @return
     */
    ResultVO<Void> delete(Integer roleId);

    /**
     * 更新角色
     *
     * @param roleVO
     * @return
     */
    ResultVO<Void> update(RoleVO roleVO);


    /**
     * 角色获取
     *
     * @param roleId
     * @return
     */
    ResultVO<RoleVO> select(Integer roleId);


    /**
     * 角色分配初始化
     * @param roleId
     * @return
     */
    ResultVO<RoleAssignVO> assignInit(Integer roleId);

    /**
     * 角色分配用户
     * @param roleId
     * @param assignedIdList
     * @return
     */
    ResultVO<Void> assign(Integer roleId, List<Integer> assignedIdList);




    /**
     * 分配功能初始化
     * @param roleId
     * @return
     */
    ResultVO<RoleAuthInitVO> authFuncInit(Integer roleId);


    /**
     * 分配功能
     * @param roleId
     * @param funcKeyList
     * @return
     */
    ResultVO<Void> authFunc(Integer roleId, List<String> funcKeyList);


    /**
     * 分配功能预先检查
     *
     * @param roleId
     * @param funcKeyList
     * @return
     */
    ResultVO<RoleAuthCheckVO> authFuncCheck(Integer roleId, List<String> funcKeyList);
}
