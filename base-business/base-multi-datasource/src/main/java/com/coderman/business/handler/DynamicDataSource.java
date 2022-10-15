package com.coderman.business.handler;

import com.coderman.service.util.ServletContextUtil;
import com.coderman.service.util.SpringContextUtil;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Primary
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Map<String,String> datasourcePackageMap =  new HashMap<>();


    private static String defaultDataSourceName = null;


    @PostConstruct
    public void initMethod(){


        Map<Object,Object> targetDataSources = new HashMap<>();

        String domain = ServletContextUtil.getServletContext().getInitParameter("domain");

        DataSource defaultDataSource = null;

        // 获取所有数据源
        Map<String, DataSource> dataSourceMap = SpringContextUtil.getApplicationContext().getBeansOfType(DataSource.class);

        if(MapUtils.isNotEmpty(dataSourceMap)){


            Set<Map.Entry<String, DataSource>> entrySet = dataSourceMap.entrySet();
            for (Map.Entry<String, DataSource> dataSourceEntry : entrySet) {

                // 多数据源列表,排除datasource本身
                if(!(dataSourceEntry.getValue() instanceof DynamicDataSource)){


                    defaultDataSource = dataSourceEntry.getValue();
                    defaultDataSourceName =dataSourceEntry.getKey();

                }


                // 默认数据源
                if(dataSourceEntry.getKey().toLowerCase().contains(domain.toLowerCase())){

                    defaultDataSource =dataSourceEntry.getValue();
                    defaultDataSourceName = dataSourceEntry.getKey();

                }
            }

            this.setTargetDataSources(targetDataSources);
            assert defaultDataSource != null;
            this.setDefaultTargetDataSource(defaultDataSource);

        }


    }

    @Override
    protected Object determineCurrentLookupKey() {
        return defaultDataSourceName;
    }

    public static String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }

    public static Map<String,String> getDatasourcePackageMap(){
        return datasourcePackageMap;
    }


}
