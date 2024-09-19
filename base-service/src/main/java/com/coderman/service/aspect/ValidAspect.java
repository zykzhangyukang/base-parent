package com.coderman.service.aspect;

import com.coderman.api.constant.AopConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.swagger.constant.SwaggerConstant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author coderman
 * @Title: 参数校验切面
 * @Description: TOD
 * @date 2022/5/921:36
 */
@Component
@Aspect
@Order(value = AopConstant.VALID_ASPECT_ORDER)
public class ValidAspect {

    @Around("(execution(* com.coderman..controller..*(..))) && @annotation(io.swagger.annotations.ApiImplicitParams))")
    public Object around(ProceedingJoinPoint point) throws Throwable{

        boolean flag = false;


        // 获取当前方法的对象
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        // 获取方法自定义对象类型参数名称列表
        Map<Integer,String> paramNameMap =  new HashMap<>();
        String paramNameArray[] = methodSignature.getParameterNames();
        Class paramTypeArray[] = methodSignature.getParameterTypes();
        Method method = methodSignature.getMethod();

        int i=0;
        for (Class cls : paramTypeArray) {

            // 判断是否是Java原生类型
            if(isJavaClass(cls)){
                paramNameMap.put(i,paramNameArray[i]);
            }

            i ++;
        }

        // 文件类型参数列表
        Set<String> fileNameSet = new HashSet<>();

        // 获取当前方法的swagger 注解
        Set<String> fieldNameSet = new HashSet<>();
        ApiImplicitParams apiImplicitParams = method.getAnnotation(ApiImplicitParams.class);

        if(null!=apiImplicitParams){
            ApiImplicitParam[] apiImplicitParamsArray = apiImplicitParams.value();
            for (ApiImplicitParam apiImplicitParam : apiImplicitParamsArray) {

                if(StringUtils.isBlank(apiImplicitParam.name())){
                    return ResultUtil.getFail("Swagger参数输入为空");
                }

                if(!apiImplicitParam.required()){
                    continue;
                }

                // 获取文件类型参数
                if(SwaggerConstant.DATA_FILE.equals(apiImplicitParam.dataType())){
                    fileNameSet.add(apiImplicitParam.name().trim());
                }else {
                    fieldNameSet.add(apiImplicitParam.name().trim());
                }
            }
        }

        // 获取所有参数
        if(!CollectionUtils.isEmpty(fileNameSet) || !CollectionUtils.isEmpty(fieldNameSet)){

            Set<String> fieldResultSet = new HashSet<>();

            int fileResult = 0;

            // 获取方法的所有参数值
            Object[] params = point.getArgs();
            int j=0;

            for (Object obj : params) {

                // 排除掉request,response 对象
                if(obj == null || (obj instanceof ServletRequest && !(obj instanceof MultipartHttpServletRequest)) || obj instanceof ServletResponse){
                    j ++;
                    continue;
                }

                // 文件类型对象
                if(!CollectionUtils.isEmpty(fileNameSet) && obj instanceof MultipartHttpServletRequest){

                    // 转换成多部分的request
                    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) obj;

                    // 获取request中的所有附件
                    MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();

                    if(!multiFileMap.isEmpty()){
                        fileResult ++;
                    }

                    j++;
                    continue;
                }


                // 文件类型对象
                if(!CollectionUtils.isEmpty(fileNameSet) && obj instanceof MultipartFile){
                    MultipartFile multipartFile = (MultipartFile) obj;

                    if(StringUtils.isNoneEmpty(multipartFile.getOriginalFilename())){

                        fileResult ++;
                    }

                    j++;
                    continue;
                }


                //JAVA 原生类参数
                if(isJavaClass(obj.getClass())){

                    // 判断Java原生类是否有值
                    if(!paramNameMap.isEmpty() && paramNameMap.containsKey(j) && fieldNameSet.contains(paramNameMap.get(j))){
                        if(StringUtils.isNotBlank(obj.toString())){
                            fieldResultSet.add(paramNameMap.get(j));
                        }else {
                            return ResultUtil.getWarn("Swagger输入的参数与当前方法不一致");
                        }
                    }

                }else {

                    // 获取注解的字段信息
                    for (String fieldName : fieldNameSet) {

                        Field field = this.getField(obj.getClass(),fieldName);
                        if(field ==null){
                            continue;
                        }

                        // 判断是否有值
                        Object userIdObj = field.getDeclaringClass().getDeclaredMethod("get" + StringUtils.capitalize(field.getName())).invoke(obj);
                        if(null!=userIdObj && StringUtils.isNotBlank(userIdObj.toString())){
                            fieldResultSet.add(fieldName);
                        }
                    }

                }

                j++;

            }

            // 判断当前对象中注解上的必填项
            if((fileNameSet.size() + fieldNameSet.size()) == (fileResult + fieldResultSet.size())){
                flag = true;
            }

        }else {
            flag = true;
        }

        if(flag){

            return point.proceed();
        }else {
            return ResultUtil.getWarn("参数为空,检验失败！");
        }

    }

    private Field getField(Class<?> obj, String fieldName) {

        Field field;

        if(obj.getName().equals(Object.class.getName())){
            return null;
        }

        Field[] declaredFields = obj.getDeclaredFields();
        if(!ArrayUtils.isEmpty(declaredFields)){
            for (Field declaredField : declaredFields) {
                if(declaredField.getName().equalsIgnoreCase(fieldName)){
                    return declaredField;
                }
            }
        }

        field = getField(obj.getSuperclass(),fieldName);


        return field;
    }

    /**
     * 判断是Java 原生类型
     * @param cls
     * @return
     */
    private boolean isJavaClass(Class cls) {
        return cls!=null && cls.getClassLoader() == null;
    }
}
