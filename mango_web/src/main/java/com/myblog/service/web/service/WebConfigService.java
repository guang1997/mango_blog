package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.WebConfigDto;

/**
 * <p>
 * web配置表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-31
 */
public interface WebConfigService extends IService<WebConfig>, ServiceConvertHandler<WebConfig, WebConfigDto> {

    WebConfigDto getWebConfig() throws Exception;
}
