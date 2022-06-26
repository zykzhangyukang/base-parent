package com.coderman.api.vo;

import lombok.Data;

/**
 * @author coderman
 * @Title: 分页对象
 * @Description: TOD
 * @date 2022/1/1521:32
 */
@Data
public class PageVO<T> {

    /**
     * 总页数
     */
    private long total = 0;

    /**
     * 数据
     */
    private T dataList;


    public PageVO() {
    }

    public PageVO(long total, T dataList) {
        this.total = total;
        this.dataList = dataList;
    }
}
