package com.coderman.api.vo;

import com.coderman.api.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * 当前页码
     */
    private int currPage;

    /**
     * 页码显示条数
     */
    private int pageRow = CommonConstant.SYS_PAGE_SIZE;

    /**
     * 总页数
     */
    @JsonIgnore
    private int totalPage;

    /**
     * 总条数
     */
    private long totalRow;

    /**
     * 数据集合
     */
    private T dataList;


    public PageVO(long totalRow, T dataList) {
        this.totalRow = totalRow;
        this.dataList = dataList;
    }


    public PageVO(long totalRow, T dataList,int currPage, int pageRow) {
        this.currPage = currPage;
        this.pageRow = pageRow;
        this.totalRow = totalRow;
        this.dataList = dataList;
    }
}
