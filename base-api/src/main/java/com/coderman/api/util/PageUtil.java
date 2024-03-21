package com.coderman.api.util;


import com.coderman.api.constant.CommonConstant;
import com.coderman.api.vo.PageVO;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author coderman
 */
public class PageUtil {


    /**
     * 分页参数
     *
     * @param conditionMap map
     * @param currentPage  当前页面
     * @param pageSize     每页显示
     * @return
     */
    public static Map<String, Object> getConditionMap(Map<String, Object> conditionMap, Integer currentPage, Integer pageSize) {

        Assert.notNull(conditionMap, "conditionMap is null");

        if(Objects.isNull(currentPage)){
            currentPage = 1;
        }

        if(Objects.isNull(pageSize)){
            pageSize = CommonConstant.SYS_PAGE_SIZE;
        }

        Integer offset = currentPage < 1 ? 1 : (currentPage - 1) * pageSize;

        conditionMap.put("offset", offset);
        conditionMap.put("size", pageSize);
        return conditionMap;
    }


    /**
     * 获取分页结果
     * @param count
     * @param dataList
     * @param currentPage
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> PageVO<List<T>> getPageVO(Long count, List<T> dataList, Integer currentPage, Integer pageSize) {
        return new PageVO<>(count, dataList, currentPage, pageSize);
    }
}
