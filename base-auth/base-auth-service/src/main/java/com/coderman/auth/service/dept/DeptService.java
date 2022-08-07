package com.coderman.auth.service.dept;


import com.coderman.api.vo.ResultVO;
import com.coderman.auth.model.dept.DeptModel;

import java.util.List;

/**
 * @author coderman
 * @date 2022/3/1216:14
 */
public interface DeptService {

    /**
     * 所有部门信息
     *
     * @return
     */
    ResultVO<List<DeptModel>> list();

}
