package com.myblog.service.web.service;

import java.util.Map;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.Sort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.SortDto;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
public interface SortService extends IService<Sort>, ServiceConvertHandler<Sort, SortDto> {

    Map<String, Object> getSortByPage(SortDto sortDto) throws Exception;
}
