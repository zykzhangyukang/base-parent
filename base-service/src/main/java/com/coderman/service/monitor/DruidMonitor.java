package com.coderman.service.monitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.coderman.api.vo.SqlMonitorVO;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ：zhangyukang
 * @date ：2025/02/10 14:25
 */
@Component
@Data
public class DruidMonitor {

    private static final Logger log = LoggerFactory.getLogger(DruidMonitor.class);
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    /**
     * 是否开启
     */
    @Value("${druid.monitor.enable:true}")
    private boolean druidMonitor;
    /**
     * 启动延时
     */
    @Value("${druid.monitor.delay.seconds:30}")
    private int delay;
    /**
     * 慢SQL耗时阈值
     */
    @Value("${druid.monitor.sql.timeout:5000}")
    private int sqlTimeOut;
    /**
     * 慢SQL条数
     */
    @Value("${druid.monitor.sql.topN:5}")
    private int sqlTopN;
    /**
     * 应用标识
     */
    @Value("${spring.application.name:}")
    private String applicationName;

    public DruidMonitor() {
    }

    @PostConstruct
    private void init() {
        this.start();
    }

    private void start() {
        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(() -> {
            try {
                this.druidMonitor();
            } catch (Exception e) {
                log.error("druidMonitor error:{}", e.getMessage(), e);
            }
        }, 1L, this.delay, TimeUnit.SECONDS);
    }

    /**
     * 监控逻辑
     */
    private void druidMonitor() {
        if (!Boolean.TRUE.equals(this.druidMonitor)) {
            log.warn("druid.monitor.enable={}", this.druidMonitor);
        } else {

            DruidStatManagerFacade druidStatManagerFacade = DruidStatManagerFacade.getInstance();
            if (druidStatManagerFacade == null) {
                log.warn("druidStatManagerFacade is null!");
            } else {

                druidStatManagerFacade.getDataSourceStatDataList(true).forEach((objectMap) -> {
                    // 数据源层面监控
                    Integer identity = (Integer) objectMap.get("Identity");
                    String jdbc = StringUtils.substringBefore((String) objectMap.get("URL"), "?");
                    this.dataSourceMonitor(objectMap, jdbc);

                    // SQL层面监控
                    List<Map<String, Object>> list = druidStatManagerFacade.getSqlStatDataList(identity);
                    this.sqlMonitor(list, jdbc);

                });
                druidStatManagerFacade.resetSqlStat();
            }
        }
    }

    /**
     * SQL层面的监控
     *
     * @param list 信息
     * @param jdbc jdbc
     */
    private void sqlMonitor(List<Map<String, Object>> list, String jdbc) {

        String jsonString = JSON.toJSONString(list);
        List<SqlMonitorVO> sqlMonitorEntities = JSON.parseObject(jsonString, new TypeReference<List<SqlMonitorVO>>() {
        });

        // top10
        List<SqlMonitorVO> sqlList = sqlMonitorEntities.stream().filter(e -> e.getMaxTimespan() > this.sqlTimeOut)
                .sorted(Comparator.comparing(SqlMonitorVO::getMaxTimespan).reversed())
                .limit(this.sqlTopN)
                .peek(e -> {
                    String sql = SQLUtils.format(e.getSQL(), JdbcConstants.MYSQL);
                    e.setSQL(sql.replaceAll("\\s+", " "));
                })
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(sqlList)) {
            log.warn("druid_slow_sql:{},{}", jdbc, JSON.toJSONString(sqlList));
        }
    }

    /**
     * 数据源层面的监控
     *
     * @param objectMap 信息
     * @param jdbc      jdbc
     */
    private void dataSourceMonitor(Map<String, Object> objectMap, String jdbc) {

        Integer maxActive = Optional.ofNullable((Integer) objectMap.get("MaxActive")).orElse(0);
        Integer activeCount = Optional.ofNullable((Integer) objectMap.get("ActiveCount")).orElse(0);
        Integer waitThreadCount = Optional.ofNullable((Integer) objectMap.get("WaitThreadCount")).orElse(0);
        if (activeCount >= maxActive) {
            log.error("druid_connect_full:{},{},waitThreadCount:{}", jdbc, JSON.toJSONString(objectMap), waitThreadCount);
        }
    }
}
