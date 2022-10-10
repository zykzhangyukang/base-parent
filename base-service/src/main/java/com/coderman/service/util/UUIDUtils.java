package com.coderman.service.util;

import java.util.UUID;

/**
 * @author coderman
 * @description: uuid工具类
 * @date 2022/6/2521:04
 */
public class UUIDUtils {


    /**
     * 生成主键uuid
     *
     * @return
     */
    public synchronized static String getPrimaryValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
