package com.myblog.service.admin.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 文件上传配置类
 *
 * @author 李斯特
 * 2022年4月4日
 */
@Component
@ConfigurationProperties(prefix="file.upload")
@Data
public class FileUploadProperties {

    /**
     * 头像大小限制
     */
    private Long avatarMaxSize;

    /**
     * 头像格式
     */
    private Set<String> avatarImgType;
}
