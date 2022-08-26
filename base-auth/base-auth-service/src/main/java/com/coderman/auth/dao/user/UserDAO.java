package com.coderman.auth.dao.user;

import com.coderman.auth.model.user.UserExample;
import com.coderman.auth.model.user.UserModel;
import com.coderman.auth.vo.user.UserVO;
import com.coderman.mybatis.dao.BaseDAO;

import java.util.List;
import java.util.Map;

public interface UserDAO extends BaseDAO<UserModel, UserExample> {



    /**
     * 用户列表
     *
     * @param conditionMap 查询条件
     * @return
     */
    List<UserVO> selectPage(Map<String, Object> conditionMap);


    /**
     * 分页总条数
     *
     * @param conditionMap 查询条件
     * @return
     */
    Long countPage(Map<String, Object> conditionMap);
}