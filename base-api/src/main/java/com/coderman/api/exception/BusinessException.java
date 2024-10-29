package com.coderman.api.exception;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author coderman
 * @Title: 业务异常
 * @Description: TOD
 * @date 2022/3/518:59
 */
public class BusinessException extends RuntimeException{

    @ApiModelProperty(value = "错误code")
    private Integer errorCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
