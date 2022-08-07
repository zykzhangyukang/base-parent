package com.coderman.auth.service.func;


import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.func.FuncQueryVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.auth.vo.func.MenuVO;

import java.util.List;

/**
 * @author coderman
 * @Title: 功能服务
 * @Description: TOD
 * @date 2022/3/1915:38
 */
public interface FuncService {

    /**
     * 获取功能树
     *
     * @return
     */
    ResultVO<FuncVO> listTree();



    /**
     * 功能列表
     * @param currentPage
     * @param pageSize
     * @param queryVO
     * @return
     */
    ResultVO<PageVO<List<FuncVO>>> page(Integer currentPage, Integer pageSize, FuncQueryVO queryVO);


    /**
     * 保存功能
     * @param funcVO
     * @return
     */
    ResultVO<Void> save(FuncVO funcVO);


    /**
     * 更新功能
     * @param funcVO
     * @return
     */
    ResultVO<Void> update(FuncVO funcVO);


    /**
     * 删除功能
     * @param funcId
     * @return
     */
    ResultVO<Void> delete(Integer funcId);


    /**
     * 获取功能
     * @param funcId
     * @return
     */
    ResultVO<FuncVO> select(Integer funcId);

    /**
     * 功能解绑用户
     *
     * @param funcId
     * @return
     */
    ResultVO<Void> deleteUserBind(Integer funcId);

    /**
     * 功能解绑资源
     *
     * @param funcId
     * @return
     */
    ResultVO<Void> deleteResourceBind(Integer funcId);

    /**
     * 查询菜单树
     *
     * @param userId
     * @return
     */
    ResultVO<List<MenuVO>> selectMenusTreeByUserId(Integer userId);


    /**
     * 查询功能按钮key
     *
     * @param userId
     * @return
     */
    ResultVO<List<String>> selectFuncKeyListByUserId(Integer userId);
}
