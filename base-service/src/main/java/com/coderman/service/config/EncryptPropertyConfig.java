package com.coderman.service.config;

import com.coderman.service.util.DesUtil;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptPropertyConfig {


    /**
     * 配置文件解密
     *
     * @return {@link EncryptablePropertyResolver}
     */
    @Bean
    public EncryptablePropertyResolver encryptablePropertyResolver() {

        return value -> {

            if (StringUtils.isBlank(value)) {

                return value;
            }

            if (value.trim().startsWith("DES@")) {

                return DesUtil.decrypt(value.trim().substring(4));
            }


            return value;
        };
    }

}
