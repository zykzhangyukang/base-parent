package com.coderman.business.handler;

/**
 * @author coderman
 */
public class DataSourceHolder {

    private static final ThreadLocal<String> DATA_SOURCE_HOLDER = new ThreadLocal<>();


    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        DATA_SOURCE_HOLDER.set(dataSource);
    }


    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        return DATA_SOURCE_HOLDER.get();
    }


    /**
     * 清除数据源类型
     */
    public static void clearDataSource() {
        DATA_SOURCE_HOLDER.remove();
    }
}
