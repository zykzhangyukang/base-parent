package com.coderman.sync.exception;

import lombok.Data;

/**
 * 同步系统异常
 */
@Data
public class SyncException extends RuntimeException{

    String message;

    private ErrorCodeEnum errorCodeEnum;

    public SyncException(String message) {
        super(message);
        this.message = message;
    }


    public SyncException(ErrorCodeEnum errorCodeEnum,String message) {
        super(message);
        this.errorCodeEnum = errorCodeEnum;
        this.message = message;
    }
}
