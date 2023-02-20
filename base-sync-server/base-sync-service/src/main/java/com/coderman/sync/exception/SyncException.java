package com.coderman.sync.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同步系统异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SyncException extends RuntimeException{


    private ErrorCodeEnum errorCodeEnum;


    public SyncException(ErrorCodeEnum codeEnum){

        super();
        this.errorCodeEnum = codeEnum;
    }


    public SyncException(ErrorCodeEnum errorCodeEnum,String message) {
        super(message);
        this.errorCodeEnum = errorCodeEnum;
    }

    public ErrorCodeEnum getCode(){

        return this.errorCodeEnum;
    }
}
