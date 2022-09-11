package com.coderman.swagger.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author coderman
 * @Title: swagger配置
 * @Description: TOD
 * @date 2022/3/169:58
 */
@Configuration
@EnableSwagger2
@SuppressWarnings("all")
public class SwaggerConfig {


    /**
     * 扫描本项目的接口
     *
     * @return
     */
    @Bean
    public Docket customerDocket() {

        return new Docket(DocumentationType.SWAGGER_2).groupName("rest")
                .apiInfo(apiInfo())
                .select()
                .paths(controllerPaths())
                .build()
                .useDefaultResponseMessages(false)
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"));
    }


    /**
     * 本项目controller路径选择器
     * @return
     */
    @SuppressWarnings("all")
    private Predicate<String> controllerPaths() {
        return PathSelectors.any();
    }




    /**
     * 扫描提供给外部的Api
     *
     * @return
     */
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2).groupName("rpc")
                .apiInfo(apiInfo())
                .select()
                .paths(apiPath()).build().useDefaultResponseMessages(false)
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"));
    }


    /**
     * 外部api路径选择器
     *
     * @return
     */
    private Predicate<String> apiPath() {
        return PathSelectors.regex("^[a-z0-9]{2,20}/*/api/.*");
    }



    /**
     * 文档介绍
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("项目文档").version("1.0.0").build();
    }

}
