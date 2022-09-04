package com.coderman.auth.service.user;


import com.coderman.api.exception.BusinessException;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.user.UserAssignVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.erp.vo.AuthUserVO;

import java.util.List;

/**
 * @author coderman
 * @Description: TOD
 * @date 2022/2/2711:41
 */
public interface UserService {

    /**
     * 用户列表
     *
     * @param currentPage
     * @param pageSize
     * @param queryVO
     * @return
     */
    ResultVO<PageVO<List<UserVO>>> page(Integer currentPage, Integer pageSize, UserVO queryVO);

    /**
     * 用户新增
     *
     * @param userVO
     * @return
     */
    ResultVO<Void> save(UserVO userVO);


    /**
     * 用户删除
     *
     * @param userId
     * @return
     */
    ResultVO<Void> delete(Integer userId);

    /**
     * 更新用户
     *
     * @param userVO
     * @return
     */
    ResultVO<Void> update(UserVO userVO);


    /**
     * 用户详情
     *
     * @param userId
     * @return
     */
    ResultVO<UserVO> select(Integer userId);


    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    ResultVO<UserVO> selectUserByName(String username);


    /**
     * 启用用户
     *
     * @param userId
     * @return
     */
    ResultVO<Void> updateEnable(Integer userId);


    /**
     * 禁用用户
     *
     * @param userId
     * @return
     */
    ResultVO<Void> updateDisable(Integer userId);


    /**
     * 用户分配初始化
     *
     * @param userId
     * @return
     */
    ResultVO<UserAssignVO> selectAssignInit(Integer userId);

    /**
     * 用户分配角色
     *
     * @param userId
     * @param assignedIdList
     * @return
     */
    ResultVO<Void> updateAssign(Integer userId, List<Integer> assignedIdList);

    /**
     * 查看用户拥有的角色信息
     *
     * @param userId
     * @return
     */
    ResultVO<List<String>> selectRoleNames(Integer userId);


    /**
     * 设置密码
     *
     * @param password
     * @param userId
     * @return
     */
    ResultVO<Void> updatePassword(Integer userId, String password);


    /**
     * 用户登入
     *
     * @param userVO
     * @return
     */
    ResultVO<AuthUserVO> login(UserVO userVO) throws BusinessException;


    /**
     * 获取用户信息
     *
     * @return
     */
    ResultVO<UserInfoVO> info(String token);


    /**
     * 根据token获取用户信息
     *
     * @param token token
     * @return
     */
    ResultVO<AuthUserVO> getUserByToken(String token);


    /**
     * 用户注销登入
     *
     * @param token 令牌
     * @return
     */
    ResultVO<Void> logout(String token);


    /**
     * 用户刷新登入
     *
     * @return
     */
    ResultVO<String> refreshLogin(String token);
}
