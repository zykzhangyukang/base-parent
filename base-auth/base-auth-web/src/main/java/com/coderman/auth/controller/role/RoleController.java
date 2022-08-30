package com.coderman.auth.controller.role;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.role.RoleService;
import com.coderman.auth.vo.role.RoleAssignVO;
import com.coderman.auth.vo.role.RoleAuthCheckVO;
import com.coderman.auth.vo.role.RoleAuthInitVO;
import com.coderman.auth.vo.role.RoleVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author coderman
 * @Title: 角色管理
 * @Description: TOD
 * @date 2022/2/2711:55
 */
@RestController
@RequestMapping(value = "/${domain}/role")
public class RoleController {


    @Resource
    private RoleService roleService;

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "角色分配用户")
    @PostMapping(value = "/assign")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色id",required = true),
            @ApiImplicitParam(name = "assignedIdList",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_OBJECT,value = "分配的用户id集合",required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> assign(@RequestParam(value = "roleId") Integer roleId,
                                 @RequestParam(value = "assignedIdList") List<Integer> assignedIdList){
        return this.roleService.assign(roleId,assignedIdList);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "角色分配功能初始化")
    @GetMapping(value = "/auth/func/init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色id",required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RoleAuthInitVO",value = {"allTreeList", "roleId", "roleName", "funcKeyList"})
    })
    public ResultVO<RoleAuthInitVO> authFuncInit(@RequestParam(value = "roleId") Integer roleId){
        return this.roleService.authFuncInit(roleId);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "角色分配功能预先检查")
    @PostMapping(value = "/auth/func/check")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色id",required = true),
            @ApiImplicitParam(name = "funcKeyList",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "功能key计集合",required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RoleAuthCheckVO",value = {"insertList", "delList"})
    })
    public ResultVO<RoleAuthCheckVO> authFuncCheck(@RequestParam(value = "roleId") Integer roleId,
                                                   @RequestParam(value = "funcKeyList") List<String> funcKeyList){
        return this.roleService.authFuncCheck(roleId,funcKeyList);
    }



    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "角色分配功能")
    @PostMapping(value = "/func/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色id",required = true),
            @ApiImplicitParam(name = "funcKeyList",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "功能key计集合",required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
    })
    public ResultVO<Void> authFunc(@RequestParam(value = "roleId") Integer roleId,
                                             @RequestParam(value = "funcKeyList") List<String> funcKeyList){
        return this.roleService.authFunc(roleId,funcKeyList);
    }



    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "角色分配初始化")
    @GetMapping(value = "/assign/init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RoleAssignVO",value = {"assignedIdList", "userList", "roleId"})
    })
    public ResultVO<RoleAssignVO> assignInit(@RequestParam(value = "roleId") Integer roleId){
        return this.roleService.assignInit(roleId);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "角色列表")
    @GetMapping(value = "/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",paramType = SwaggerConstant.PARAM_FORM,value = "当前页",dataType = SwaggerConstant.DATA_INT,required = true),
            @ApiImplicitParam(name = "pageSize",paramType = SwaggerConstant.PARAM_FORM,value = "每页大小",dataType = SwaggerConstant.DATA_INT,required = true),
            @ApiImplicitParam(name = "roleName",paramType = SwaggerConstant.PARAM_FORM,value = "角色名称",dataType = SwaggerConstant.DATA_STRING),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"dataList",  "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "RoleVO",value = {"roleDesc", "createTime", "roleId", "roleName", "updateTime", "userNameList"})
    })
    public ResultVO<PageVO<List<RoleVO>>> page(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                               @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
                                               @ApiIgnore RoleVO queryVO) {
        return this.roleService.page(currentPage, pageSize,queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "保存角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "角色名称",required = true),
            @ApiImplicitParam(name = "roleDesc",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "角色描述",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/save")
    public ResultVO<Void> save(@ApiIgnore RoleVO roleVO) {
        return this.roleService.save(roleVO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "更新角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色Id",required = true),
            @ApiImplicitParam(name = "roleName",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "角色名称",required = true),
            @ApiImplicitParam(name = "roleDesc",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "角色描述",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/update")
    public ResultVO<Void> update(@ApiIgnore RoleVO roleVO) {
        return this.roleService.update(roleVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "删除角色")
    @GetMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色Id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@RequestParam(value = "roleId") Integer roleId) {
        return this.roleService.delete(roleId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "获取角色")
    @GetMapping(value = "/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "角色Id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RoleVO",value = {"roleName","roleDesc","roleId"})
    })
    public ResultVO<RoleVO> select(@RequestParam(value = "roleId") Integer roleId) {
        return this.roleService.select(roleId);
    }



}
