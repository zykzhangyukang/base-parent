package com.coderman.sync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sync.callback")
@Data
public class CallbackConfig {

    private List<Callback> destList;



    @Data
    public static class Callback implements Serializable {

        private String project;

        private String url;
    }

}