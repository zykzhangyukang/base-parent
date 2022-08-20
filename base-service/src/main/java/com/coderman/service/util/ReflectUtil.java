package com.coderman.service.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtil {

    /**
     * 判断一个类是java类型还是用户自定义类型
     *
     * @param clz
     * @return
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }


    /**
     * 获取超类的参数类型，取第一个参数类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Class getClassGenricType(final Class clazz) { return getClassGenricType(clazz,0);}


    /**
     * 获取父类的泛型类
     * @param clazz
     * @return
     */
    public static Class<?> getSuperClassGenericType(Class<?> clazz){
        ParameterizedType pType = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class<?>) pType.getActualTypeArguments()[0];
    }


    /**
     * 根据索引获取超类的参数类型
     * @param clazz 超类类型
     * @param index 索引
     * @return
     */
    public static Class getClassGenricType(final  Class clazz,final int index){
        Type genType = clazz.getGenericSuperclass();

        if(!(genType instanceof ParameterizedType)){
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if(index >= params.length || index <0){
            return Object.class;
        }

        if(!(params[index] instanceof Class)){
            return Object.class;
        }

        return (Class) params[index];
    }


    /**
     * 获取所有字段
     * @param obj
     * @return
     * @param <T>
     */
    public static <T> List<Field> getAllField(Class<T> obj){

        List<Field> list = new ArrayList<>();

        try {

            if(obj.getName().equals(Object.class.getName())){
                return list;
            }

            Field[] fields = obj.getDeclaredFields();
            if(!ArrayUtils.isEmpty(fields)){
                list.addAll(Arrays.asList(fields));
            }

            List<Field> listParent = getAllField(obj.getSuperclass());
            list.addAll(listParent);


        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 获取指定字段
     * @param obj
     * @param fieldName
     * @return
     * @param <T>
     */
    public static <T> Field getField(Class<T> obj, String fieldName){

        Field field = null;

        try {
            if(obj.getName().equals(Object.class.getName())){
                return null;
            }

            Field[] fields = obj.getDeclaredFields();
            if(!ArrayUtils.isEmpty(fields)){
                for (Field tempField : fields) {
                    if(tempField.getName().equalsIgnoreCase(fieldName)){
                        return tempField;
                    }
                }
            }


            field = getField(obj.getSuperclass(), fieldName);

        }catch (Exception e){
            e.printStackTrace();
        }

        return field;
    }


    /**
     * 指定字段值
     * @param obj
     * @param filedName
     * @param fieldValue
     * @param overFlag
     * @param <T>
     */
    public static <T> void setFiledValue(Object obj,String filedName,Object fieldValue ,boolean overFlag){

        Field field = getField(obj.getClass(), filedName);
        if(field!=null){

            try {
                Object userIdObj = field.getDeclaringClass().getDeclaredMethod("get" + StringUtils.capitalize(field.getName()), field.getType())
                        .invoke(obj, fieldValue);

                if(userIdObj == null || StringUtils.isEmpty(userIdObj.toString()) || overFlag){

                    field.getDeclaringClass()
                            .getDeclaredMethod("set"+StringUtils.capitalize(field.getName()),field.getType())
                            .invoke(obj,fieldValue);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
