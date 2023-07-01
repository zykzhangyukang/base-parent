package com.coderman.auth.controller.func;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.func.FuncService;
import com.coderman.auth.vo.func.FuncQueryVO;
import com.coderman.auth.vo.func.FuncVO;
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
 * @Title: 功能
 * @Description: TOD
 * @date 2022/3/1915:34
 */
@RestController
@RequestMapping(value = "/${domain}/func")
public class FuncController {

    @Autowired
    private FuncService funcService;

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "获取功能树")
    @GetMapping(value = "/list/tree")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "FuncVO",value = {"funcTreeVOList","funcVOList"}),
            @ApiReturnParam(name = "FuncTreeVO",value = {"funcName", "funcKey", "createTime", "updateTime", "childrenList", "funcId", "parentId"})
    })
    public ResultVO<FuncVO> listTree(){

        return this.funcService.listTree();
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "功能解绑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_QUERY,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true)
    })
    @GetMapping(value = "/delete/user/bind")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> deleteUserBind(@RequestParam(value = "funcId") Integer funcId){

        return this.funcService.deleteUserBind(funcId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "功能解绑资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_QUERY,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true)
    })
    @GetMapping(value = "/delete/resc/bind")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> deleteResourceBind(@RequestParam(value = "funcId") Integer funcId){

        return this.funcService.deleteResourceBind(funcId);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "功能列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "当前页",required = true),
            @ApiImplicitParam(name = "pageSize",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "每页显示大小",required = true),
            @ApiImplicitParam(name = "funcKey",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "功能key"),
            @ApiImplicitParam(name = "funcName",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "功能名称"),
            @ApiImplicitParam(name = "rescUrl",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_STRING,value = "功能url")
    })
    @GetMapping(value = "/page")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO",value = {"dataList",  "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "FuncVO",value = {"parentFuncName","funcSort","dirHide","userVOList","rescVOList","funcName", "funcKey", "createTime","funcType","funcIcon",
                    "updateTime", "childrenList", "funcId", "parentId","rescIdList"})
    })
    public ResultVO<PageVO<List<FuncVO>>> page(@RequestParam(value = "currentPage") Integer currentPage,
                                               @RequestParam(value = "pageSize") Integer pageSize, @ApiIgnore FuncQueryVO queryVO){

        return this.funcService.page(currentPage,pageSize,queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "保存功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcName",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_STRING,value = "功能名称",required = true),
            @ApiImplicitParam(name = "funcKey",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_STRING,value = "功能key",required = true),
            @ApiImplicitParam(name = "parentId",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "父级Id",required = true),
            @ApiImplicitParam(name = "funcType",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能类型",required = true),
            @ApiImplicitParam(name = "funcSort",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能排序",required = true),
            @ApiImplicitParam(name = "dirShow",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能排序"),
            @ApiImplicitParam(name = "rescIdList",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_OBJECT,value = "资源id"),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/save")
    public ResultVO<Void> save(@RequestBody @ApiIgnore FuncVO funcVO) {
        return this.funcService.save(funcVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST,value = "更新功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true),
            @ApiImplicitParam(name = "funcName",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_STRING,value = "功能名称",required = true),
            @ApiImplicitParam(name = "funcKey",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_STRING,value = "功能key",required = true),
            @ApiImplicitParam(name = "funcType",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能类型",required = true),
            @ApiImplicitParam(name = "funcSort",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能排序",required = true),
            @ApiImplicitParam(name = "dirShow",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_INT,value = "功能排序"),
            @ApiImplicitParam(name = "rescIdList",paramType = SwaggerConstant.PARAM_BODY,dataType = SwaggerConstant.DATA_OBJECT,value = "资源id"),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/update")
    public ResultVO<Void> update(@RequestBody @ApiIgnore FuncVO funcVO) {
        return this.funcService.update(funcVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "删除资源")
    @GetMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "功能id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@RequestParam(value = "funcId") Integer funcId) {
        return this.funcService.delete(funcId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET,value = "获取资源")
    @GetMapping(value = "/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "funcId",paramType = SwaggerConstant.PARAM_FORM,dataType = SwaggerConstant.DATA_INT,value = "资源Id",required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "FuncVO",value = {"parentFuncName","funcSort","dirHide","userVOList","rescVOList","rescIdList",
                    "funcName", "funcKey", "createTime", "updateTime", "childrenList", "funcId", "parentId","funcType"})
    })
    public ResultVO<FuncVO> select(@RequestParam(value = "funcId") Integer funcId) {
        return this.funcService.select(funcId);
    }




}
