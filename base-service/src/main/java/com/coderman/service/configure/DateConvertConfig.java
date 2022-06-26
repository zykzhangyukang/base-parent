package com.coderman.service.configure;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author coderman
 * @Title: 全局日期格式转换器
 * @date 2022/6/1912:16
 */
@Component
public class DateConvertConfig implements Converter<String, Date> {

    //静态初始化定义日期字符串参数列表（需要转换的）
    private static final List<String> PARAM_LIST = new ArrayList<>();

    private static final String param0 = "yyyy";
    private static final String param1 = "yyyy-MM";
    private static final String param2 = "yyyy-MM-dd";
    private static final String param3 = "yyyy-MM-dd HH:mm";
    private static final String param4 = "yyyy-MM-dd HH:mm:ss";

    static {
        PARAM_LIST.add(param0);
        PARAM_LIST.add(param1);
        PARAM_LIST.add(param2);
        PARAM_LIST.add(param3);
        PARAM_LIST.add(param4);
    }


    public Date parseDate(String source, String format) {
        Date date = null;
        try {
            //日期格式转换器
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    @Override
    public Date convert(String source) {

        if (StringUtils.isEmpty(source)) {
            return null;
        }

        source = source.trim();   //去除首尾空格

        if (source.matches("^\\d{4}")) {
            return parseDate(source, PARAM_LIST.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, PARAM_LIST.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, PARAM_LIST.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, PARAM_LIST.get(3));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, PARAM_LIST.get(4));
        } else {
            throw new IllegalArgumentException("还未定义该种字符串转Date的日期转换格式 --> 【日期格式】：" + source);
        }
    }
}
