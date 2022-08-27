package com.coderman.auth.api.auth;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author coderman
 * @Title: 用户服务提供给权限系统的api
 * @Description: TOD
 * @date 2022/3/513:17
 */
@RestController
@RequestMapping(value = "/user/2/auth")
public interface User2AuthApi {


    /**
     * 根据用户账号获取用户基本信息
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/select/user/by/name")
    ResultVO<UserVO> selectUserByName(@RequestParam(value = "username") String username);



    /**
     * 根据用户账号获取用户资源
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/select/user/resources/by/name")
    ResultVO<List<ResourceVO>> selectUserResourcesByName(@RequestParam(value = "username") String username);
}
