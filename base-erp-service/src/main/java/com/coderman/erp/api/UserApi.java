package com.coderman.erp.api;

import com.coderman.api.vo.ResultVO;
import com.coderman.erp.vo.AuthUserVO;

/**
 * @author coderman
 */
public interface UserApi {

    /**
     * 根据token获取用户信息
     * @param token token
     * @return
     */
    ResultVO<AuthUserVO> getUserByToken(String token);
}
