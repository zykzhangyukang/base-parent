package com.coderman.auth.controller.user;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.user.UserService;
import com.coderman.auth.vo.user.UserAssignVO;
import com.coderman.auth.vo.user.UserInfoVO;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.erp.vo.AuthUserVO;
import com.coderman.service.util.AddressUtil;
import com.coderman.service.util.IpUtil;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author coderman
 * @Title: 用户模块
 * @date 2022/2/2711:37
 */
@RestController
@RequestMapping(value = "/${domain}/user")
public class UserController {

    @Autowired
    private UserService userService;



    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "用户登入")
    @PostMapping(value = "/login")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING),
            @ApiImplicitParam(name = "password", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO",value = {"realName", "deptCode", "username", "token"})
    })
    public ResultVO<AuthUserVO> login(UserVO userVO) {
        return this.userService.login(userVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "用户登入")
    @PostMapping(value = "/refresh/login")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO",value = {"realName", "deptCode", "username", "token"})
    })
    public ResultVO<String> refreshLogin(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME) String token) {
        return this.userService.refreshLogin(token);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "注销登入")
    @PostMapping(value = "/logout")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "AuthUserVO",value = {"realName", "deptCode", "username", "token"})
    })
    public ResultVO<Void> logout(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME,required = false) String token) {
        return this.userService.logout(token);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "获取用户信息")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserInfoVO", value = {"deptName", "realName", "userStatus", "userId", "deptCode", "username","roles","funcKeys","menus"}),
    })
    @GetMapping(value = "/info")
    public ResultVO<UserInfoVO> info(@RequestHeader(value = CommonConstant.USER_TOKEN_NAME,required = false) String token){
        return this.userService.info(token);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取用户拥有的角色名称")
    @GetMapping(value = "/select/role/names")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<List<String>> selectRoleNames(@RequestParam(value = "userId") Integer userId) {
        return this.userService.selectRoleNames(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "用户分配初始化")
    @GetMapping(value = "/role/update/init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserAssignVO", value = {"assignedIdList", "roleList", "userId"})
    })
    public ResultVO<UserAssignVO> selectAssignInit(@RequestParam(value = "userId") Integer userId) {
        return this.userService.selectAssignInit(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "用户角色分配")
    @PostMapping(value = "/role/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true),
            @ApiImplicitParam(name = "assignedIdList", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_OBJECT, value = "分配的角色id集合", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateAssign(@RequestParam(value = "userId") Integer userId,
                                       @RequestParam(value = "assignedIdList") List<Integer> assignedIdList) {
        return this.userService.updateAssign(userId, assignedIdList);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取用户信息")
    @GetMapping(value = "/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "UserVO", value = {"createTime", "updateTime", "username", "realName", "userStatus", "deptCode", "userId"})
    })
    public ResultVO<UserVO> select(@RequestParam(value = "userId") Integer userId) {
        return this.userService.select(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "用户列表")
    @GetMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"dataList", "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "UserVO", value = {"realName", "deptName", "password", "userStatus", "createTime", "updateTime", "userId", "deptCode", "username"})
    })
    public ResultVO<PageVO<List<UserVO>>> page(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, UserVO queryVO) {
        return this.userService.page(currentPage, pageSize, queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "更新密码")
    @PostMapping(value = "/update/password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true),
            @ApiImplicitParam(name = "password", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "密码", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public ResultVO<Void> updatePassword(Integer userId, String password) {

        return this.userService.updatePassword(userId, password);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "启用用户")
    @GetMapping(value = "/update/enable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> updateEnable(@RequestParam(value = "userId") Integer userId) {
        return this.userService.updateEnable(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "禁用用户")
    @GetMapping(value = "/update/disable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> disable(@RequestParam(value = "userId") Integer userId) {
        return this.userService.updateDisable(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "保存用户")
    @PostMapping(value = "/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "用户账号", required = true),
            @ApiImplicitParam(name = "password", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "密码", required = true),
            @ApiImplicitParam(name = "realName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "用户姓名", required = true),
            @ApiImplicitParam(name = "userStatus", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户状态", required = true),
            @ApiImplicitParam(name = "deptCode", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "部门编号", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> save(@ApiIgnore UserVO userVO) {
        return this.userService.save(userVO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "删除用户")
    @GetMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@RequestParam(value = "userId") Integer userId) {
        return this.userService.delete(userId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "更新用户")
    @PostMapping(value = "/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "用户账号", required = true),
            @ApiImplicitParam(name = "realName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "用户姓名", required = true),
            @ApiImplicitParam(name = "userStatus", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "用户状态", required = true),
            @ApiImplicitParam(name = "deptCode", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "部门编号", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> update(@ApiIgnore UserVO userVO) {
        return this.userService.update(userVO);
    }


}
