package com.myblog.service.admin.config;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 阿里云OSS配置类
 *
 * @author 李斯特
 * 2022年4月4日
 */
@Component
@ConfigurationProperties(prefix="qiniu.oss")
@Data
public class QiNiuYunOssProperties {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainName;

    @Bean
    public Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public UploadManager getUploadManager(){
        return new UploadManager(new Configuration());
    }
}
