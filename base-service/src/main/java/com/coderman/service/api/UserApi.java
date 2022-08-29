package com.coderman.service.api;

import com.coderman.api.vo.AuthUserVO;
import com.coderman.api.vo.ResultVO;

public interface UserApi {

    /**
     * 根据token获取用户信息
     * @param token token
     * @return
     */
    ResultVO<AuthUserVO> getUserByToken(String token);
}
