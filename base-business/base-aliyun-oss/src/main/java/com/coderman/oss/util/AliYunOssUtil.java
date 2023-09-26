package com.coderman.oss.util;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.coderman.oss.config.AliYunOssProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：zhangyukang
 * @date ：2023/09/25 10:40
 */
@Component
public class AliYunOssUtil {

    private final AliYunOssProperties aliYunOssProperties;

    /**
     * 获取阿里云OSS客户端对象
     */
    private OSSClient ossClient;

    public AliYunOssUtil(AliYunOssProperties aliYunOssProperties) {
        this.aliYunOssProperties = aliYunOssProperties;
    }


    /**
     * 获取阿里云OSS客户端对象
     */
    private OSSClient getOssClient(){
        if(ossClient == null){
            ClientConfiguration config = new ClientConfiguration();
            ossClient = new OSSClient(aliYunOssProperties.getEndPoint(), new DefaultCredentialProvider(aliYunOssProperties.getAccessKeyId() ,aliYunOssProperties.getAccessKeySecret()), config);
        }
        return ossClient;
    }


}
