package com.coderman.auth.controller.api;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.api.auth.User2AuthApi;
import com.coderman.auth.api.auth.User2AuthService;
import com.coderman.auth.vo.resource.ResourceVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author coderman
 * @Title: user系统提供给auth的api
 * @Description: TOD
 * @date 2022/3/1616:25
 */
@RestController
public class User2AuthApiController implements User2AuthApi {

    @Autowired
    private User2AuthService user2AuthService;


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "提供给权限系统获取基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",paramType = SwaggerConstant.PARAM_QUERY,dataType = SwaggerConstant.DATA_STRING,value = "用户名",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserVO", value = {"deptName", "realName", "password", "userStatus", "createTime", "updateTime", "userId", "deptCode",
                    "password","username","roleList"})
    })
    @Override
    public ResultVO<UserVO> selectUserByName(String username) {
        return this.user2AuthService.selectUserByName(username);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "提供给权限系统获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",paramType = SwaggerConstant.PARAM_QUERY,dataType = SwaggerConstant.DATA_STRING,value = "用户名",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserInfoVO", value = {"deptName", "realName", "userStatus", "userId", "deptCode", "username","roles","funcKeys","menus"}),
    })
    @Override
    public ResultVO<UserInfoVO> selectUserInfoByName(String username) {
        return this.user2AuthService.selectUserInfoByName(username);
    }


    @SneakyThrows
    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "提供给权限系统获取用户资源信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",paramType = SwaggerConstant.PARAM_QUERY,dataType = SwaggerConstant.DATA_STRING,value = "用户名",required = true)
    })
    @Override
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "ResourceVO",value = {"resourceUrl","methodType"})
    })
    public ResultVO<List<ResourceVO>> selectUserResourcesByName(String username) {
        return this.user2AuthService.selectUserResourcesByName(username);
    }


}
