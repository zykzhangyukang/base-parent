package com.coderman.auth.controller.dept;

import com.coderman.api.vo.ResultVO;
import com.coderman.auth.model.dept.DeptModel;
import com.coderman.auth.service.dept.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author coderman
 * @Title: 部门
 * @Description: TOD
 * @date 2022/3/1216:12
 */
@RestController
@RequestMapping(value = "/${domain}/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping(value = "/list")
    public ResultVO<List<DeptModel>> list() {
        return this.deptService.list();
    }

}
