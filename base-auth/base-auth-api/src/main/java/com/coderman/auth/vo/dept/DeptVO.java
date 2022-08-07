package com.coderman.auth.vo.dept;

import com.coderman.api.model.BaseModel;
import com.coderman.auth.vo.user.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 部门VO
 * @Description: TOD
 * @date 2022/3/1410:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptVO extends BaseModel {

    /**
     * 用户信息
     */
    private List<UserVO> userList;
}
