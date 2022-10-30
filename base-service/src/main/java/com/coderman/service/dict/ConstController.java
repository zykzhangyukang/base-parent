package com.coderman.service.dict;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/const")
public class ConstController {


    @GetMapping(value = "/list")
    public ResultVO<List<ConstItems>> list(){
        return ResultUtil.getSuccessList(ConstItems.class,ConstService.getAllConstList());
    }
}
