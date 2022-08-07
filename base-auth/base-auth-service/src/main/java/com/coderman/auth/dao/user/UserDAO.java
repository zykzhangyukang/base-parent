package com.coderman.auth.dao.user;

import com.coderman.auth.model.user.UserExample;
import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.mybatis.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO extends BaseDAO<UserModel, UserExample> {



    /**
     * 用户列表
     *
     * @param queryVO
     * @return
     */
    List<UserVO> page(@Param(value = "queryVO") UserVO queryVO);
}