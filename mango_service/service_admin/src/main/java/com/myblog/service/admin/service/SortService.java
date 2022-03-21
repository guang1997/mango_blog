package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Sort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.base.common.Response;

import java.text.ParseException;
import java.util.Set;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
public interface SortService extends IService<Sort> {

    Response getSortByPage(SortDto sortDto) throws ParseException;

    Response addSort(SortDto sortDto);

    Response editSort(SortDto sortDto);

    Response delSorts(Set<String> ids);
}
