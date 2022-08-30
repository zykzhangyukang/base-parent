package com.coderman.auth.controller.resource;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.resource.ResourceService;
import com.coderman.auth.vo.resource.ResourceVO;
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
 * @Title: 资源
 * @Description: TOD
 * @date 2022/3/199:02
 */
@RestController
@RequestMapping(value = "/${domain}/resource")
public class ResourceController {

    @Resource
    private ResourceService resourceService;


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "资源列表")
    @GetMapping(value = "/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", paramType = SwaggerConstant.PARAM_FORM, value = "当前页", dataType = SwaggerConstant.DATA_INT, required = true),
            @ApiImplicitParam(name = "pageSize", paramType = SwaggerConstant.PARAM_FORM, value = "每页大小", dataType = SwaggerConstant.DATA_INT, required = true),
            @ApiImplicitParam(name = "resourceUrl", paramType = SwaggerConstant.PARAM_FORM, value = "资源url", dataType = SwaggerConstant.DATA_STRING),
            @ApiImplicitParam(name = "methodType", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "请求方式")
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"dataList", "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "ResourceVO", value = {"resourceName", "resourceUrl", "resourceDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<PageVO<List<ResourceVO>>> page(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                   @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize, @ApiIgnore ResourceVO queryVO) {
        return this.resourceService.page(currentPage, pageSize, queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "保存资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源名称", required = true),
            @ApiImplicitParam(name = "resourceUrl", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源url", required = true),
            @ApiImplicitParam(name = "resourceDomain", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "所属系统", required = true),
            @ApiImplicitParam(name = "methodType", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "请求方式", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/save")
    public ResultVO<Void> save(@ApiIgnore ResourceVO resourceVO) {
        return this.resourceService.save(resourceVO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "更新资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源id", required = true),
            @ApiImplicitParam(name = "resourceName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源名称", required = true),
            @ApiImplicitParam(name = "resourceUrl", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源url", required = true),
            @ApiImplicitParam(name = "resourceDomain", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "所属系统", required = true),
            @ApiImplicitParam(name = "methodType", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "请求方式", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/update")
    public ResultVO<Void> update(@ApiIgnore ResourceVO resourceVO) {
        return this.resourceService.update(resourceVO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "搜索资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "关键字", required = false)
    })
    @GetMapping(value = "/search")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "ResourceVO", value = {"resourceName", "resourceUrl", "resourceDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<List<ResourceVO>> search(@RequestParam(value = "keyword") String keyword) {

        return this.resourceService.search(keyword);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "删除资源")
    @GetMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "资源Id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@RequestParam(value = "resourceId") Integer resourceId) {
        return this.resourceService.delete(resourceId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取资源")
    @GetMapping(value = "/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "资源Id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "ResourceVO", value = {"resourceUrl", "resourceName", "resourceId", "resourceDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<ResourceVO> select(@RequestParam(value = "resourceId") Integer resourceId) {
        return this.resourceService.select(resourceId);
    }


}
