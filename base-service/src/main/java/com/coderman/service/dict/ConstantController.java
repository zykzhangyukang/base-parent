package com.coderman.service.dict;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/${domain}/constant")
public class ConstantController {

    @Autowired
    private ConstantService constantService;

    @GetMapping(value = "/list")
    public ResultVO<List<ConstantItems>> list(){
        List<ConstantItems> allConstList = this.constantService.getAllConstList();
        return ResultUtil.getSuccessList(ConstantItems.class,allConstList);
    }
}
