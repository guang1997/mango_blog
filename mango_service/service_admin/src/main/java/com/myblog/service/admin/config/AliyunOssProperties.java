package com.myblog.service.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 阿里云OSS配置类
 *
 * @author 李斯特
 * 2022年4月4日
 */
@Component
@ConfigurationProperties(prefix="aliyun.oss")
@Data
public class AliyunOssProperties {

    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
    private Map<String, String> modulNames;
}
