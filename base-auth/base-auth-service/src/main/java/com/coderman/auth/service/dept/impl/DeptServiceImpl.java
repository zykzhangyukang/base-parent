package com.coderman.auth.service.dept.impl;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import com.coderman.auth.dao.dept.DeptDAO;
import com.coderman.auth.model.dept.DeptModel;
import com.coderman.auth.service.dept.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author coderman
 * @date 2022/3/1216:15
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDAO deptDAO;

    @Override
    public ResultVO<List<DeptModel>> list() {
        List<DeptModel> deptModels = this.deptDAO.selectByExample(null);
        return ResultUtil.getSuccessList(DeptModel.class, deptModels);
    }
}
