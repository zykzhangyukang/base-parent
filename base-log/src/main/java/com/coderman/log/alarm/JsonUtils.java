package com.coderman.log.alarm;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;

/**
 * Json 工具类
 * @author zhangyukang
 *
 */
public class JsonUtils {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String toString(Object obj){
		return toJson(obj);
	}
	
	public static String toJson(Object obj){
		try{
			StringWriter writer = new StringWriter();
			mapper.writeValue(writer, obj);
			return writer.toString();
		}catch(Exception e){
			throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
		}
	}
	
	public static <T> T toBean(Class<T> entityClass, String jsonString){
		try {
			return mapper.readValue(jsonString, entityClass);
		} catch (Exception e) {
			throw new RuntimeException("JSON【"+jsonString+"】转对象时出错", e);
		}
	}
	
	/**
	 * 用于对象通过其他工具已转为JSON的字符形式，这里不需要再加上引号
	 * @param obj
	 */
	public static String getJsonSuccess(Object obj){
		return getJsonSuccess(obj, null);
	}
	
	public static String getJsonSuccess(Object obj, String message) {
		if(obj == null){
			return "{\"code\":200,\"msg\":\""+message+"\"}";
		}else{
			try{
				return "{\"code\":200,\"result\":"+toString(obj)+",\"msg\":\""+message+"\"}";
			}catch(Exception e){
				throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
			}
		}
	}
	
	public static String getJsonError(Object obj){
		return getJsonError(obj, null);
	}
	
	public static String getJsonError(Object obj, String message) {
		if(obj == null){
			return "{\"code\":500,\"message\":\""+message+"\"}";
		}else{
			try{
				obj = parseIfException(obj);
				return "{\"code\":500,\"result\":"+toString(obj)+",\"msg\":\""+message+"\"}";
			}catch(Exception e){
				throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
			}
		}
	}
	
	public static Object parseIfException(Object obj){
		if(obj instanceof Exception){
			return getErrorMessage((Exception) obj, null);
		}
		return obj;
	}
	
	public static String getErrorMessage(Exception e, String defaultMessage){
		return defaultMessage;
	}
	
	public static ObjectMapper getMapper() {
		return mapper;
	}
}
