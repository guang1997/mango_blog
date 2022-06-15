package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.security.entity.WebVisit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.dto.WebVisitDto;

import java.util.Set;

/**
 * <p>
 * Web访问记录表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-15
 */
public interface WebVisitService extends IService<WebVisit>, ServiceConvertHandler<WebVisit, WebVisitDto> {

    Response getWebVisitByPage(WebVisitDto webVisitDto) throws Exception;

    Response delWebVisit(Set<String> ids);

    int getWebVisitCount();

    Response getWebVisitGroupByBehavior() throws Exception;

    Response getVisitByWeek() throws Exception;
}
