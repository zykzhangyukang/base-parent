package com.coderman.business.handler;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DynamicDataSource extends AbstractRoutingDataSource implements ApplicationContextAware {

    private static final Map<String, String> datasourcePackageMap = new HashMap<>();

    private static String defaultDataSourceName = null;

    private static ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() {

        Map<Object, Object> targetDataSources = new HashMap<>();

        String domain = System.getProperty("domain");

        DataSource dataSource = null;

        // 获取所有数据源
        Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);

        if (MapUtils.isNotEmpty(dataSourceMap)) {

            Set<Map.Entry<String, DataSource>> entrySet = dataSourceMap.entrySet();
            for (Map.Entry<String, DataSource> dataSourceEntry : entrySet) {

                // 多数据源列表,排除datasource本身
                if (!(dataSourceEntry.getValue() instanceof DynamicDataSource)) {

                    targetDataSources.put(dataSourceEntry.getKey(),dataSourceEntry.getValue());
                }

                // 默认数据源
                if (dataSourceEntry.getKey().toLowerCase().contains(domain.toLowerCase())) {

                    dataSource = dataSourceEntry.getValue();
                    defaultDataSourceName = dataSourceEntry.getKey();
                }
            }

            this.setTargetDataSources(targetDataSources);
            assert dataSource != null;
            this.setDefaultTargetDataSource(dataSource);
        }

        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return defaultDataSourceName;
    }

    public static String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }

    public static Map<String, String> getDatasourcePackageMap() {
        return datasourcePackageMap;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DynamicDataSource.applicationContext = applicationContext;
    }
}
