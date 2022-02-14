/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
public class MySecurityProperties {

    /**
     * Request Headers ： Authorization
     */
    private String header;

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    private Integer expiresSecond;

    /**
     * 是否七天免登陆 单位毫秒
     */
    private Long rememberMeExpiresSecond;
}
