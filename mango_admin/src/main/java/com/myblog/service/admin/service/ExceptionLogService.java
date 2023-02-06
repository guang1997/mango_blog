package com.myblog.service.admin.service;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.security.entity.ExceptionLog;
import com.myblog.service.security.entity.dto.ExceptionLogDto;

/**
 * <p>
 * 异常信息表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-12-16
 */
public interface ExceptionLogService extends IService<ExceptionLog>,
    ServiceConvertHandler<ExceptionLog, ExceptionLogDto> {

    Map<String, Object> getExceptionByPage(ExceptionLogDto exceptionLogDto) throws Exception;

}
