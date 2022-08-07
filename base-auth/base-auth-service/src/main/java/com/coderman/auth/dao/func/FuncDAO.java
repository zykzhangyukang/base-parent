package com.coderman.auth.dao.func;

import com.coderman.auth.model.func.FuncModel;
import com.coderman.auth.model.func.FuncModelExample;
import com.coderman.auth.vo.func.FuncQueryVO;
import com.coderman.auth.vo.func.FuncVO;
import com.coderman.auth.vo.func.MenuVO;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FuncDAO extends BaseDAO<FuncModel, FuncModelExample> {

    /**
     * 功能列表
     *
     * @param queryVO
     * @return
     */
    List<FuncVO> page(@Param(value = "queryVO") FuncQueryVO queryVO);

    /**
     * 根据功能id获取用户
     *
     * @param funcId
     * @return
     */
    List<UserVO> selectUserListByFuncId(@Param(value = "funcId") Integer funcId);


    /**
     * 根据功能id查询资源
     *
     * @param funcId
     * @return
     */
    List<ResourceVO> selectResListByFuncId(@Param(value = "funcId") Integer funcId);


    /**
     * 插入返回组件
     *
     * @param insert
     */
    void insertSelectiveReturnKey(FuncModel insert);


    /**
     * 查询功能信息
     *
     * @param funcId
     * @return
     */
    FuncVO selectFuncInfo(@Param(value = "funcId") Integer funcId);


    /**
     * 根据用户id获取所有功能
     *
     * @param userId
     * @return
     */
    List<FuncModel> selectFuncListByUserId(Integer userId);


    /**
     * 根据用户id获取所有目录
     *
     * @param userId
     * @return
     */
    List<MenuVO> selectAllMenusByUserId(Integer userId);


    /**
     * 根据用户id获取所有功能按钮
     *
     * @param userId
     * @return
     */
    List<String> selectFuncKeyListByUserId(Integer userId);
}