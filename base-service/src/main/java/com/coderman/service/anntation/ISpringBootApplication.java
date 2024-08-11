package com.coderman.service.anntation;

import com.coderman.api.constant.CommonConstant;
import com.coderman.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.lang.annotation.*;

/**
 * @author coderman
 * @Title: 自定义springboot启动类注解
 * @date 2022/6/1814:01
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, ActiveMQAutoConfiguration.class})
@EnableCustomSwagger2
@ComponentScan(basePackages = {CommonConstant.BASE_PACKAGE},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
@MapperScan(basePackages = {CommonConstant.BASE_DAO_PACKAGE, CommonConstant.BASE_MAPPER_PACKAGE})
public @interface ISpringBootApplication {

}
