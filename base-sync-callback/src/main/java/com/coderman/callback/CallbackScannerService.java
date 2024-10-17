package com.coderman.callback;

import com.coderman.api.exception.BusinessException;
import com.coderman.service.anntation.ClassPathScanningComponentProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Lazy(false)
@Component
@Slf4j
public class CallbackScannerService implements SmartLifecycle {


    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final Map<String, CallbackMeta> callbackMetaMap = new HashMap<>();

    @Override
    public void start() {
        if (this.initialized.compareAndSet(false, true)) {
            log.info("Starting CallbackScannerService...");
            ClassPathScanningComponentProvider provider = new ClassPathScanningComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(Service.class));
            Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("com/coderman/" + System.getProperty("domain") + "/**/service");

            for (BeanDefinition beanDefinition : beanDefinitions) {
                try {
                    Method[] declaredMethods = Class.forName(beanDefinition.getBeanClassName()).getDeclaredMethods();
                    for (Method method : declaredMethods) {
                        for (Annotation annotation : method.getDeclaredAnnotations()) {
                            if (annotation instanceof SyncCallback) {
                                this.processSyncCallback(annotation, method, beanDefinition);
                                break;
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    log.error("Failed to scan for sync callback functions", e);
                    throw new RuntimeException("Failed to scan sync callback functions", e);
                }
            }
            log.info("CallbackScannerService started successfully.");
        }
    }

    private void processSyncCallback(Annotation annotation, Method method, BeanDefinition beanDefinition) throws ClassNotFoundException {
        SyncCallback syncCallback = (SyncCallback) annotation;

        if (this.callbackMetaMap.containsKey(syncCallback.value())) {
            throw new BusinessException("同一个同步计划不允许有多个回调");
        }

        if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != SyncMsg.class) {
            throw new BusinessException("回调方法参数异常");
        }

        this.callbackMetaMap.put(syncCallback.value(), new CallbackMeta(syncCallback.value(), method.getName(), Class.forName(beanDefinition.getBeanClassName()).getInterfaces()[0]));
    }

    public CallbackMeta getCallbackMeta(String planCode) {
        return this.callbackMetaMap.get(planCode);
    }

    @Override
    public void stop() {
        this.initialized.set(false);
        log.info("CallbackScannerService stopped.");
    }

    @Override
    public boolean isRunning() {
        return this.initialized.get();
    }
}
