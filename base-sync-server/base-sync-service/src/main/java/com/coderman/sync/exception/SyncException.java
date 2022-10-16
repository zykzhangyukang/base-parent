package com.coderman.sync.exception;

/**
 * 同步系统异常
 */
public class SyncException extends RuntimeException{

    String message;

    public SyncException(String message) {
        super(message);
    }
}
