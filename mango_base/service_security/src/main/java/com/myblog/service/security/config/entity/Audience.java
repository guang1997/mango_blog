package com.myblog.service.security.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT相关配置
 */
@Data
@ConfigurationProperties(prefix = "audience")
@Component
public class Audience {
    private String base64Secret;
    private int expiresSecond;
}