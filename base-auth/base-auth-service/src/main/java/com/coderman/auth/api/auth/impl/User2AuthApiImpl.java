package com.coderman.auth.api.auth.impl;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.api.auth.User2AuthService;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.service.resource.ResourceService;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.func.MenuVO;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author coderman
 * @Title: user 2 auth
 * @date 2022/3/513:32
 */
@Service
public class User2AuthApiImpl implements User2AuthService {


    @Autowired
    private UserService userService;

    @Autowired
    private FuncService funcService;

    @Autowired
    private ResourceService resourceService;


    @Override
    public ResultVO<UserVO> selectUserByName(String username) {
        return this.userService.selectUserByName(username);
    }



    @Override
    public ResultVO<List<ResourceVO>> selectUserResourcesByName(String username) {
        List<ResourceVO> resourceVOList = this.resourceService.selectResourceListByUsername(username);
        return ResultUtil.getSuccessList(ResourceVO.class, resourceVOList);
    }


}
