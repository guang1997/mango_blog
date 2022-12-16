package com.myblog.service.admin.service;

import java.text.ParseException;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.ExceptionLog;
import com.myblog.service.admin.entity.dto.ExceptionLogDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

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

    Response getExceptionByPage(ExceptionLogDto exceptionLogDto) throws Exception;

}
