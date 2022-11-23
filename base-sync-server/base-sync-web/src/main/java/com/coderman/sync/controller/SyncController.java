package com.coderman.sync.controller;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.util.MsgBuilder;
import com.coderman.sync.util.ProjectEnum;
import com.coderman.sync.util.SyncUtil;
import com.coderman.sync.vo.PlanMsg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class SyncController {

    @GetMapping(value = "/sync")
    public ResultVO<Void> sync() {
        PlanMsg planMsg = MsgBuilder
                .create("insert_sku_pim_catalog", ProjectEnum.SKU, ProjectEnum.PIM)
                .addIntList("insert_sku_pim_catalog", Arrays.asList(1, 2))
                .build();
        SyncUtil.sync(planMsg);

        return ResultUtil.getSuccess();
    }
}
