package com.coderman.api.exception;

/**
 * @author coderman
 * @Title: 业务异常
 * @Description: TOD
 * @date 2022/3/518:59
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
