package com.coderman.erp.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@SuppressWarnings("all")
@ConfigurationProperties(prefix = "auth.erp")
public class AuthErpConfig  {

    @ApiModelProperty(value = "权限安全码")
    private String authSecurityCode;

    @ApiModelProperty(value = "权限服务器节点")
    private String authServerArr;
}
