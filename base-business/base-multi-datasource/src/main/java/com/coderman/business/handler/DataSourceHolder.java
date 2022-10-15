package com.coderman.business.handler;

public class DataSourceHolder {

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();


    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        dataSourceHolder.set(dataSource);
    }


    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        return dataSourceHolder.get();
    }


    /**
     * 清除数据源类型
     */
    public static void clearDataSource() {
        dataSourceHolder.remove();
    }
}
