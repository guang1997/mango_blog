package com.myblog.service.admin.service;


import com.myblog.service.base.common.Response;

import java.util.Map;

/**
 * <p>
 * 监控页面 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-28
 */
public interface MonitorService {

    Map<String, Object> getServers();
}
