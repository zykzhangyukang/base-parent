package com.coderman.erp.listener;

import com.coderman.erp.aop.AuthAspect;
import com.coderman.erp.constant.RedisConstant;
import com.coderman.redis.annotaion.RedisChannelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RefreshRescListener {

    @Resource
    private AuthAspect authAspect;

    @RedisChannelListener(channelName = RedisConstant.TOPIC_REFRESH_RESC)
    public void refreshSystemResc(String messageContent) {
        try {

            String project = System.getProperty("domain");
            this.authAspect.refreshSystemAllRescMap(project);

            log.info("refreshSystemResc |  刷新同步计划完成！ -> {} | project:{}", messageContent,project);

        } catch (Exception e) {
            log.error("refreshSystemResc |  刷新系统资源失败！| error:{}", e.getMessage(), e);
        }
    }
}
