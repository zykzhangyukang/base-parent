package com.coderman.service.publisher;

import com.coderman.service.event.ConfigChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@EnableAsync
@Component
public class ConfigChangePublisher {

    @Resource
    private ApplicationEventPublisher eventPublisher;


    /**
     * 发布事件
     * @param changeKey 变更key
     * @param oldValue 老值
     * @param newValue 新值
     */
    public void publish(String changeKey,String oldValue,String newValue){

        ConfigChangeEvent configChangeEvent = new ConfigChangeEvent(this,changeKey,oldValue,newValue );
        eventPublisher.publishEvent(configChangeEvent);
    }
}
