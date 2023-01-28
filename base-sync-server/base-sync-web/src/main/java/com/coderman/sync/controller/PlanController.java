package com.coderman.sync.controller;

import com.coderman.api.vo.ResultVO;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import com.coderman.sync.service.PlanService;
import com.coderman.sync.vo.PlanVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    @PostMapping(value = "/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", paramType = SwaggerConstant.DATA_INT, dataType = SwaggerConstant.DATA_INT, value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", paramType = SwaggerConstant.DATA_INT, dataType = SwaggerConstant.DATA_INT, value = "分页大小", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "PlanVO", value = {"srcProject", "destDb", "destProject", "planContent", "updateTime", "createTime", "srcDb",
                    "uuid", "planCode", "status"}),
    })
    @ApiReturnIgnore
    public List<PlanVO> page(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                                               @RequestParam(value = "pageSize",defaultValue = "15") Integer pageSize, PlanVO queryVO) {

        return this.planService.page(currentPage, pageSize, queryVO);
    }


    @GetMapping(value = "/content")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.DATA_STRING, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    public ResultVO<String> selectContent(String uuid) {


        return this.planService.selectContent(uuid);
    }

}
