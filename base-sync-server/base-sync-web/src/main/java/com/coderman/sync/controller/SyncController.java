package com.coderman.sync.controller;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.sync.util.MsgBuilder;
import com.coderman.sync.util.ProjectEnum;
import com.coderman.sync.util.SyncUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class SyncController {

    @GetMapping(value = "/sync")
    public ResultVO<Void> sync() {
        SyncUtil.sync(
                MsgBuilder.create("insert_datasource1_datasource2_user", ProjectEnum.SYS1, ProjectEnum.SYS2)
                        .addIntList("update_datasource1_datasource2_user", Arrays.asList(1))
                        .build()
        );

        return ResultUtil.getSuccess();
    }
}
