package com.coderman.callback;

import lombok.Data;

@Data
public class CallbackMeta {

    private String className;

    private String methodName;

    private Class<?> instantClass;

    public CallbackMeta() {
    }

    public CallbackMeta(String className, String methodName, Class<?> instantClass) {
        this.className = className;
        this.methodName = methodName;
        this.instantClass = instantClass;
    }
}
