package com.coderman.swagger.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author coderman
 * @description: swagger 属性
 */
@Component
@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerProperties {

    @ApiModelProperty(value = "是否开启swagger")
    private Boolean enabled;

    @ApiModelProperty(value = "swagger会解析的包路径")
    private String basePackage = "";

    @ApiModelProperty(value = "swagger会解析的url规则")
    private List<String> basePath = new ArrayList<>();

    @ApiModelProperty(value = "在basePath基础上需要排除的url规则")
    private List<String> excludePath = new ArrayList<>();

    @ApiModelProperty(value = "不需要鉴权即可访问的接口")
    private List<String> excludeAuth = new ArrayList<>();

    @ApiModelProperty(value = "标题")
    private String title = "";

    @ApiModelProperty(value = "描述")
    private String description = "";

    @ApiModelProperty(value = "版本")
    private String version = "";

    @ApiModelProperty(value = "许可证")
    private String license = "";

    @ApiModelProperty(value = "许可证URL")
    private String licenseUrl = "";

    @ApiModelProperty(value = "服务条款URL")
    private String termsOfServiceUrl = "";

    @ApiModelProperty(value = "host信息")
    private String host = "";

    @ApiModelProperty(value = "联系人信息")
    private Contact contact = new Contact();


    @Data
    public static class Contact {

        @ApiModelProperty(value = "联系人")
        private String name = "";

        @ApiModelProperty(value = "联系人url")
        private String url = "";

        @ApiModelProperty(value = "联系人email")
        private String email = "";
    }
}