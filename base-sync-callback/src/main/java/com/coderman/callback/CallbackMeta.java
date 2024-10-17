package com.coderman.callback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author coderman
 */
@Data
public class CallbackMeta {

    @ApiModelProperty(value = "类名")
    private String className;

    @ApiModelProperty(value = "方法名")
    private String methodName;

    @ApiModelProperty(value = "类对象")
    private Class<?> instantClass;

    public CallbackMeta() {
    }

    public CallbackMeta(String className, String methodName, Class<?> instantClass) {
        this.className = className;
        this.methodName = methodName;
        this.instantClass = instantClass;
    }
}
