package com.coderman.sync.controller.result;

import com.alibaba.fastjson.JSONObject;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.constant.SwaggerConstant;
import com.coderman.sync.service.result.ResultService;
import com.coderman.sync.vo.ResultVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/${domain}/result")
public class ResultController {

    @Resource
    private ResultService resultService;

    @PostMapping(value = "/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_INT, value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_INT, value = "分页大小", required = true),
    })
    @ApiReturnIgnore
    public JSONObject search(@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                           @RequestParam(value = "rows", defaultValue = "20") Integer pageSize, ResultVO resultVO) throws Exception {
        return this.resultService.search(currentPage,pageSize,resultVO);
    }

    @GetMapping(value = "/repeat/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.PARAM_QUERY, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    @ApiReturnIgnore
    public com.coderman.api.vo.ResultVO<Void> repeatSync(String uuid){

        return this.resultService.repeatSync(uuid);
    }


}
