package com.coderman.auth.controller.resc;

import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.service.resc.RescService;
import com.coderman.auth.vo.resc.RescVO;
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
@RequestMapping(value = "/${domain}/resc")
public class RescController {

    @Resource
    private RescService rescService;


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "资源列表")
    @GetMapping(value = "/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", paramType = SwaggerConstant.PARAM_FORM, value = "当前页", dataType = SwaggerConstant.DATA_INT, required = true),
            @ApiImplicitParam(name = "pageSize", paramType = SwaggerConstant.PARAM_FORM, value = "每页大小", dataType = SwaggerConstant.DATA_INT, required = true),
            @ApiImplicitParam(name = "rescUrl", paramType = SwaggerConstant.PARAM_FORM, value = "资源url", dataType = SwaggerConstant.DATA_STRING),
            @ApiImplicitParam(name = "methodType", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "请求方式")
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "PageVO", value = {"dataList", "pageRow", "totalRow", "currPage", "totalPage"}),
            @ApiReturnParam(name = "RescVO", value = {"rescName", "rescUrl", "rescDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<PageVO<List<RescVO>>> page(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                               @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize, @ApiIgnore RescVO queryVO) {
        return this.rescService.page(currentPage, pageSize, queryVO);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "保存资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rescName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源名称", required = true),
            @ApiImplicitParam(name = "rescUrl", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源url", required = true),
            @ApiImplicitParam(name = "rescDomain", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "所属系统", required = true),
            @ApiImplicitParam(name = "methodType", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "请求方式", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/save")
    public ResultVO<Void> save(@ApiIgnore RescVO rescVO) {
        return this.rescService.save(rescVO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_POST, value = "更新资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rescId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源id", required = true),
            @ApiImplicitParam(name = "rescName", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源名称", required = true),
            @ApiImplicitParam(name = "rescUrl", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "资源url", required = true),
            @ApiImplicitParam(name = "rescDomain", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "所属系统", required = true),
            @ApiImplicitParam(name = "methodType", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "请求方式", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    @PostMapping(value = "/update")
    public ResultVO<Void> update(@ApiIgnore RescVO rescVO) {
        return this.rescService.update(rescVO);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "搜索资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_STRING, value = "关键字", required = false)
    })
    @GetMapping(value = "/search")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RescVO", value = {"rescName", "rescUrl", "rescDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<List<RescVO>> search(@RequestParam(value = "keyword") String keyword) {

        return this.rescService.search(keyword);
    }


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "删除资源")
    @GetMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rescId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "资源Id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"})
    })
    public ResultVO<Void> delete(@RequestParam(value = "rescId") Integer rescId) {
        return this.rescService.delete(rescId);
    }

    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "获取资源")
    @GetMapping(value = "/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rescId", paramType = SwaggerConstant.PARAM_FORM, dataType = SwaggerConstant.DATA_INT, value = "资源Id", required = true)
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "RescVO", value = {"rescUrl", "rescName", "rescId", "rescDomain", "createTime", "updateTime", "methodType"})
    })
    public ResultVO<RescVO> select(@RequestParam(value = "rescId") Integer rescId) {
        return this.rescService.select(rescId);
    }


}
