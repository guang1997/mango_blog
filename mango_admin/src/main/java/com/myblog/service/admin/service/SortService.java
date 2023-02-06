package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.Sort;
import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
public interface SortService extends IService<Sort>, ServiceConvertHandler<Sort, SortDto> {

    Map<String, Object> getSortByPage(SortDto sortDto) throws Exception;

    Boolean addSort(SortDto sortDto) throws Exception;

    Boolean editSort(SortDto sortDto) throws Exception;

    Boolean delSorts(Set<String> ids) throws Exception;
}
