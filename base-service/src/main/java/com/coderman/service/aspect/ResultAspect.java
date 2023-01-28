package com.coderman.service.aspect;

import com.coderman.api.constant.AopConstant;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.util.ReflectUtil;
import com.coderman.swagger.annotation.ApiReturnIgnore;
import com.coderman.swagger.annotation.ApiReturnParam;
import com.coderman.swagger.annotation.ApiReturnParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author coderman
 * @Title: 返回结果切面
 * @date 2022/3/1512:14
 */
@Aspect
@Component
@Slf4j
@Order(value = AopConstant.RESULT_ASPECT_ORDER)
@SuppressWarnings("all")
public class ResultAspect {


    // 清除返回记录
    private static Map<String, Set<String>> resultClearMap = null;

    @Pointcut("(execution(* com.coderman..controller..*(..))) && @annotation(com.coderman.swagger.annotation.ApiReturnParams)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{


        // 获取方法结果
        Object result = point.proceed();

        // 获取当前方法对象
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();


        // 获取当前方法上的Swagger注解
        Map<String, List<String>> returnParamsMap =  new HashMap<>();
        ApiReturnParams apiReturnParams = method.getAnnotation(ApiReturnParams.class);
        ApiReturnIgnore apiReturnIgnore = method.getAnnotation(ApiReturnIgnore.class);

        if(null !=apiReturnIgnore){

            return result;
        }

        if (null != apiReturnParams) {

            ApiReturnParam[] apiReturnParamsArray = apiReturnParams.value();

            for (ApiReturnParam apiReturnParam : apiReturnParamsArray){

                returnParamsMap.put(apiReturnParam.name() , Arrays.asList(apiReturnParam.value()));
            }
        }



        if(! (result instanceof ResultVO)){

            throw new IllegalArgumentException("ResultAspect 清除返回值信息异常: [返回值类型只能是ResultVO]");
        }

        // 获取所有参数
        ResultVO resultVO = (ResultVO) result;

        if(returnParamsMap.isEmpty()){

            return new ResultVO<>();
        }

        resultClearMap = new HashMap<>();

        // 过滤返回类型
        setTypeFilter(returnParamsMap , method.getGenericReturnType() , resultVO);

        if(!returnParamsMap.isEmpty()){

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ResultAspect清除返回信息:");
            for (Map.Entry<String,Set<String>> entry : resultClearMap.entrySet()){

                stringBuilder.append("[").append(entry.getKey()).append(entry.getValue()).append("]");
                log.warn(stringBuilder.toString());
            }
        }

        return result;
    }


    private void setTypeFilter(Map<String, List<String>> returnParamsMap, Type type, ResultVO resultVO) {

        if(type instanceof ParameterizedType){

            setReturnResult(returnParamsMap, ((ParameterizedType) type).getRawType(),resultVO);

            Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();

            for (Type typeTmp : typeArray){

                setTypeFilter(returnParamsMap , typeTmp ,resultVO);
            }

        }else {

            setReturnResult(returnParamsMap, type,resultVO);
        }
    }

    private void setReturnResult(Map<String, List<String>> returnParamsMap, Type type, Object resultVO) {

        if(Collection.class.isAssignableFrom((Class<?>) type)){
            return;
        }

        if(((Class<?>) type).getClassLoader() == null){
            return;
        }

        Object objTmpResult = getFilterObj(type,resultVO);

        if(objTmpResult == null){
            return;
        }

        if(Collection.class.isAssignableFrom(objTmpResult.getClass())){

            Collection collection = (Collection) objTmpResult;

            if(!CollectionUtils.isEmpty(collection)){

                for (Object objTemp : collection){

                    setReturnResult(returnParamsMap , type ,objTemp);
                }
            }

        }else {

            setReturnFilter(returnParamsMap,type,objTmpResult);
        }

    }

    private void setReturnFilter(Map<String, List<String>> returnParamsMap, Type type, Object resultVO) {

        List<Field> fieldList = ReflectUtil.getAllField((Class<?>) type);

        for (Field field : fieldList) {


            if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())){
                continue;
            }

            boolean flag =false;

            if(!field.isAccessible()){
                field.setAccessible(true);
                flag =true;
            }

            try {
                if(null!=field.get(resultVO) && !returnParamsMap.getOrDefault(resultVO.getClass().getSimpleName(), new ArrayList<>()).contains(field.getName())){

                    field.set(resultVO,setDefaultValue(field.getType()));

                    if(!CollectionUtils.isEmpty(resultClearMap.get(resultVO.getClass().getName()))){
                        resultClearMap.get(resultVO.getClass().getName()).add(field.getName());
                    }else {

                        Set<String> temList =  new HashSet<>();
                        temList.add(field.getName());
                        resultClearMap.put(resultVO.getClass().getName(),temList);
                    }
                }

            } catch (IllegalAccessException e) {

                e.printStackTrace();
            }


            if(flag){
                field.setAccessible(false);
            }
        }



    }

    private Object setDefaultValue(Class<?> cls) {

        if(Arrays.asList(byte.class,short.class,int.class,long.class,float.class,double.class).contains(cls)){

            return 0;
        }

        if(boolean.class.equals(cls)){
            return false;
        }

        if(char.class.equals(cls)){
            return StringUtils.EMPTY;
        }


        return null;
    }

    private Object getFilterObj(Type type, Object resultVO) {

        if(null == resultVO){
            return null;
        }

        if(!((Class<?>) type).isAssignableFrom(resultVO.getClass())){

            if(Collection.class.isAssignableFrom(resultVO.getClass())){

                Collection collection = (Collection) resultVO;

                if(!CollectionUtils.isEmpty(collection) && collection.toArray()[0].getClass().equals(type)){

                    return resultVO;
                }

                return null;
            }



            if(Map.class.isAssignableFrom(resultVO.getClass())){
                return  null;
            }

            if(resultVO.getClass() != null && resultVO.getClass().getClassLoader() ==null){
                return null;
            }

            List<Field> fieldList = ReflectUtil.getAllField(resultVO.getClass());

            for (Field field : fieldList) {

                if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())){
                    continue;
                }

                boolean flag =false;
                if(!field.isAccessible()){

                    field.setAccessible(true);
                    flag = true;
                }

                Object obj = null;
                try {
                    obj = field.get(resultVO);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(flag){
                    field.setAccessible(false);
                }

                Object resultTmp = getFilterObj(type, obj);

                if(resultTmp!=null){
                    return resultTmp;
                }

            }

            return null;
        }

        return resultVO;
    }
}
