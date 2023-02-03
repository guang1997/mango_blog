package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.WebConfigDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

/**
 * <p>
 * web配置表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-30
 */
public interface WebConfigService extends IService<WebConfig>, ServiceConvertHandler<WebConfig, WebConfigDto> {
    Response getWebConfig() throws Exception;

    Response editWebConfig(WebConfigDto webConfigDto) throws Exception;
}
