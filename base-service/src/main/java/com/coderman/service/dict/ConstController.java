package com.coderman.service.dict;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangyukang
 */
@Api(value = "本项目常量", tags = "本项目常量 [内存]")
@RestController
@RequestMapping(value = "/${domain}/const")
public class ConstController {


    @ApiOperation(httpMethod = SwaggerConstant.METHOD_GET, value = "本项目常量列表")
    @GetMapping(value = "/list")
    @ApiReturnParams({
            @ApiReturnParam(name = "ResultVO", value = {"code", "msg", "result"}),
            @ApiReturnParam(name = "ConstItems", value = {"group", "itemList"}),
    })
    public ResultVO<List<ConstItems>> list(){
        return ResultUtil.getSuccessList(ConstItems.class,ConstService.getAllConstList());
    }
}
