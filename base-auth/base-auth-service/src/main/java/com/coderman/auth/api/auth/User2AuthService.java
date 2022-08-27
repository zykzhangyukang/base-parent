package com.coderman.auth.api.auth;


import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;

import java.util.List;

/**
 * @author coderman
 * @Title: user2AuthService
 * @Description: TOD
 * @date 2022/3/1616:23
 */
public interface User2AuthService {

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    ResultVO<UserVO> selectUserByName(String username);




    /**
     * 获取用户拥有的资源
     *
     * @param username
     * @return
     */
    ResultVO<List<ResourceVO>> selectUserResourcesByName(String username);
}
