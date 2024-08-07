package com.coderman.service.config;

import com.coderman.service.util.SpringContextUtil;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author coderman
 */
public class PropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Pattern pattern = Pattern.compile("\\w+\\[\\d+\\]");

    private static final Pattern patternLetter = Pattern.compile("\\w+");

    private static final Pattern patternDigit = Pattern.compile("\\d+");


    @Override
    public synchronized void initialize(ConfigurableApplicationContext applicationContext) {

        // 解决SpringContextUtil最先初始化

        applicationContext.addBeanFactoryPostProcessor(new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
                beanDefinition.setBeanClass(SpringContextUtil.class);
                registry.registerBeanDefinition("springContextUtil", beanDefinition);
            }

            @Override
            public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

            }
        });


        // 必填配置检查
        applicationContext.getEnvironment().setRequiredProperties("domain");

        Map<String, String> configMap = new HashMap<>();
        Map<String, Map<String, String>> dictMap = new HashMap<>();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        for (PropertySource<?> propertySource : environment.getPropertySources()) {

            if (propertySource instanceof CompositePropertySource) {
                CompositePropertySource compositePropertySource = (CompositePropertySource) propertySource;
                setProperty(compositePropertySource.getPropertyNames(), compositePropertySource, environment, configMap, dictMap);
            }

            // propertySource instanceof MapPropertySource && (propertySource.getName().contains("bootstrap") || propertySource.getName().equals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME))
            if (propertySource instanceof MapPropertySource || propertySource.getName().equals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)) {

                assert propertySource instanceof MapPropertySource;
                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                setProperty(mapPropertySource.getPropertyNames(), mapPropertySource, environment, configMap, dictMap);
            }

        }

        PropertyConfig.setConfigMap(configMap);
        PropertyConfig.setDictMap(dictMap);
    }

    private void setProperty(String[] propertyNames, PropertySource<?> propertySource, ConfigurableEnvironment environment, Map<String, String> configMap, Map<String, Map<String, String>> sourceDictMap) {

        if (ArrayUtils.isEmpty(propertyNames)) {

            return;
        }

        for (String propertyName : propertyNames) {

            if (!isSpringBootProperty(propertySource, propertyName)) {
                continue;
            }


            Object objVal = propertySource.getProperty(propertyName);
            if (objVal == null || StringUtils.isBlank(objVal.toString())) {

                PropertyConfig.put(propertyName, "");
                configMap.put(propertyName, "");
                continue;
            }

            String propVal = objVal.toString().trim();
            if (propVal.startsWith("${") && propVal.endsWith("}")) {

                propVal = environment.resolvePlaceholders(propVal);
            }

            PropertyConfig.put(propertyName, propVal);
            configMap.put(propertyName, propVal);

            String nameSpace = StringUtils.substringBeforeLast(propertyName, ".");
            String key = StringUtils.substringAfterLast(propertyName, ".");

            if (pattern.matcher(key).matches()) {

                boolean regMatcher = false;
                String letterStr = null;
                String digitStr = null;


                Matcher matcherLetter = patternLetter.matcher(key);
                if (matcherLetter.find()) {

                    letterStr = matcherLetter.group();
                    regMatcher = true;

                }

                Matcher matcherDigit = patternDigit.matcher(key);
                if (regMatcher && matcherDigit.find()) {

                    digitStr = matcherDigit.group();
                } else {

                    regMatcher = false;
                }

                if (regMatcher) {

                    nameSpace += "." + letterStr;
                    key = digitStr;
                }

            }

            Map<String, String> dictMap = new HashMap<>(PropertyConfig.getDictMap(nameSpace));
            dictMap.put(key, propVal);

            PropertyConfig.putDict(nameSpace, dictMap);
            sourceDictMap.put(nameSpace, dictMap);
        }

    }

    private boolean isSpringBootProperty(PropertySource<?> propertySource, String propertyName) {

        if (propertySource.getName().equals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)) {

            return propertyName.startsWith("spring.") || propertyName.startsWith("eureka.") || propertyName.startsWith("server.") || propertyName.equals("log.file");
        }

        return true;
    }
}
