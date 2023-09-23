package com.coderman.api.util;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class DiffUtils {

    public static <T> DiffResult buildResult(T baseObj, T compareObj, String... excludeFields) {

        try {
            final DiffBuilder diffBuilder = new DiffBuilder(baseObj, compareObj, ToStringStyle.JSON_STYLE);

            // 这里使用的JavaBean相关的工具类，所以要去比对对象需要符合JavaBean的规范
            final BeanInfo beanInfo = Introspector.getBeanInfo(baseObj.getClass());
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (isExcludeField(propertyDescriptor.getName(), excludeFields)) {
                    continue;
                }

                final Method readMethod = propertyDescriptor.getReadMethod();

                final Object obj1 = readMethod.invoke(baseObj);
                final Object obj2 = readMethod.invoke(compareObj);
                if (obj1 instanceof Diffable<?>) {

                    @SuppressWarnings("unchecked")
                    Diffable<Object> diff1 = (Diffable<Object>) obj1;
                    @SuppressWarnings("unchecked")
                    Diffable<Object> diff2 = (Diffable<Object>) obj2;

                    diffBuilder.append(propertyDescriptor.getName(), diff1.diff(diff2));
                } else {
                    diffBuilder.append(propertyDescriptor.getName(), obj1, obj2);
                }
            }
            return diffBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isExcludeField(String fieldName, String... excludeFields) {
        if (excludeFields == null || excludeFields.length == 0) {
            return false;
        }
        for (String excludeField : excludeFields) {
            if (excludeField.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }
}
