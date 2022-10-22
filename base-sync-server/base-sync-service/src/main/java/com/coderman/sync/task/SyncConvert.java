package com.coderman.sync.task;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyncConvert {


    public static List<String> SUPPORT_TYPE = Arrays.asList("int","string","objectid");

    public static final String DATA_TYPE_INT = "int";
    public static final String DATA_TYPE_STRING = "int";
    public static final String DATA_TYPE_OBJECTID = "objectid";


    public static Object convert(Object origin,String targetType){


        if(origin == null){

            return null;
        }

        if(DATA_TYPE_INT.equalsIgnoreCase(targetType)){

            try {
                return Integer.parseInt(origin.toString());
            }catch (NumberFormatException e){

                return origin.toString();
            }
        }else if(DATA_TYPE_STRING.equalsIgnoreCase(targetType)){

            return origin.toString();
        }else {

            return origin;
        }

    }


    public static List<Object> convert(List<Object> paramList,String targetType){

        List<Object> result = new ArrayList<>();

        if(SUPPORT_TYPE.contains(targetType)){


            for (Object param : paramList) {

                result.add(SyncConvert.convert(param,targetType));
            }

        }else {

            result  = SyncConvert.convert(paramList);
        }

        return result;

    }

    public static List<Object> convert(List<Object> paramList){


        List<Object> result = new ArrayList<>();

        String targetType = StringUtils.EMPTY;

        for (Object paramObj : paramList) {

            if(paramObj instanceof String){

                if(StringUtils.isNotBlank(targetType) && !DATA_TYPE_STRING.equalsIgnoreCase(targetType)){

                    result = paramList;
                    break;
                }

                targetType = DATA_TYPE_STRING;
            }else if(NumberUtils.isCreatable(paramObj.toString())){

                if(StringUtils.isNotBlank(targetType) && !DATA_TYPE_INT.equalsIgnoreCase(targetType)){

                    result = paramList;
                    break;
                }

                targetType = DATA_TYPE_INT;
            }

        }

        if(CollectionUtils.isEmpty(result) && StringUtils.isNotBlank(targetType)){

            result = SyncConvert.convert(paramList,targetType);
        }

        return result;

    }

    public static List<Object[]> toArrayList(List<Object> paramList){


        List<Object[]> list= new ArrayList<>();

        list.add(SyncConvert.toArray(paramList));

        return list;

    }

    public static Object[] toArray(List<Object> paramList){

        return paramList.toArray();
    }
}
