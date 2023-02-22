package com.coderman.sync.controller.plan;

import com.alibaba.fastjson.JSONObject;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import com.coderman.swagger.constant.SwaggerConstant;
import com.coderman.sync.service.plan.PlanService;
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


    @PostMapping(value = "/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "uuid"),
            @ApiImplicitParam(name = "content", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "同步计划内容"),
    })
    public ResultVO<Void> updatePlan(@RequestBody PlanVO planVO) {

        return this.planService.updatePlan(planVO);
    }

    @PostMapping(value = "/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", paramType = SwaggerConstant.PARAM_BODY, dataType = SwaggerConstant.DATA_STRING, value = "同步计划内容"),
    })
    public ResultVO<Void> savePlan(@RequestBody PlanVO planVO) {

        return this.planService.savePlan(planVO);
    }


    @GetMapping(value = "/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = SwaggerConstant.DATA_INT, dataType = SwaggerConstant.DATA_INT, value = "当前页", required = true),
            @ApiImplicitParam(name = "limit", paramType = SwaggerConstant.DATA_INT, dataType = SwaggerConstant.DATA_INT, value = "分页大小", required = true),
    })
    @ApiReturnParams({
            @ApiReturnParam(name = "PlanVO", value = {"srcProject", "destDb", "destProject", "planContent", "updateTime", "createTime", "srcDb",
                    "uuid", "planCode", "status"}),
    })
    @ApiReturnIgnore
    public ResultVO<PageVO<List<PlanVO>>> page(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "limit", defaultValue = "30") Integer limit, PlanVO queryVO,
                                               @RequestParam(value = "sort",required = false) String sort,
                                               @RequestParam(value = "order",required = false) String order) {

        return this.planService.page(page, limit,sort,order, queryVO);
    }


    @GetMapping(value = "/content")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.DATA_STRING, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    public ResultVO<String> selectContent(String uuid) {

        return this.planService.selectContent(uuid);
    }

    @GetMapping(value = "/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.DATA_STRING, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    public ResultVO<Void> deletePlan(String uuid) {

        return this.planService.deletePlan(uuid);
    }

    @GetMapping(value = "/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", paramType = SwaggerConstant.DATA_STRING, dataType = SwaggerConstant.DATA_STRING, value = "uuid", required = true),
    })
    public ResultVO<Void> updateStatus(String uuid) {

        return this.planService.updateStatus(uuid);
    }

}
