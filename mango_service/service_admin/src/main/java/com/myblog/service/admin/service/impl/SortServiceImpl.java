package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.entity.Sort;
import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.admin.mapper.BlogMapper;
import com.myblog.service.admin.mapper.SortMapper;
import com.myblog.service.admin.service.SortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.*;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.Admin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
@Service
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements SortService {

    private static Logger LOGGER = LoggerFactory.getLogger(SortServiceImpl.class);

    @Autowired
    private BlogMapper blogMapper;

    /**
     * 分页查询分类信息
     * @param sortDto
     * @return
     */
    @Override
    public Response getSortByPage(SortDto sortDto) throws ParseException {
        Response response = Response.ok();
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(sortDto.getPage())) page = sortDto.getPage();
        if (Objects.nonNull(sortDto.getSize())) size = sortDto.getSize();
        if (StringUtils.isNotBlank(sortDto.getSortName())) {
            queryWrapper.like(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        }
        if (Objects.nonNull(sortDto.getSortLevel())) {
            queryWrapper.eq(DbConstants.Sort.SORT_LEVEL, sortDto.getSortLevel());
        }
        if (!CollectionUtils.isEmpty(sortDto.getCreateTimes()) && Objects.equals(2, sortDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(sortDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(sortDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        Page<Sort> sortPage = new Page<>(page, size);

        baseMapper.selectPage(sortPage, queryWrapper);

        List<SortDto> sortDtos = toDto(sortPage.getRecords());
        response.data(Constants.ReplyField.DATA, sortDtos);
        response.data(Constants.ReplyField.TOTAL, sortPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 添加分类
     * @param sortDto
     * @return
     */
    @Override
    public Response addSort(SortDto sortDto) {
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            LOGGER.error("addSort failed, sortName is already exist, tag:{}", sortDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Sort tag = toSort(sortDto);
        if (baseMapper.insert(tag) < 1) {
            LOGGER.error("addSort failed by unknown error, sort:{}", sortDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 编辑分类
     * @param sortDto
     * @return
     */
    @Override
    public Response editSort(SortDto sortDto) {
        Sort sort = toSort(sortDto);
        if (baseMapper.updateById(sort) < 1) {
            LOGGER.error("editSort failed by unknown error, sort:{}", sortDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @Override
    public Response delSorts(Set<String> ids) {
        // 如果分类已经绑定了博客，那么不删除该分类
        List<String> delFailedSortNames = new ArrayList<>();
        for (String id : ids) {
            QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
            blogQueryWrapper.eq(DbConstants.Blog.BLOG_SORT_ID, id);
            List<Blog> blogsBySortId = blogMapper.selectList(blogQueryWrapper);
            if (!CollectionUtils.isEmpty(blogsBySortId)) {
                Sort sort = baseMapper.selectById(id);
                LOGGER.warn("delete sort failed, the sort:{} has been bound", sort);
                delFailedSortNames.add(sort.getSortName());
                continue;
            }
            if (baseMapper.deleteById(id) < 1) {
                LOGGER.error("delSorts failed by unknown error, roleId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        if (CollectionUtils.isEmpty(delFailedSortNames)) {
            return Response.ok();
        }
        return Response.error().message(delFailedSortNames.toString() + "已绑定用户, 未删除成功");
    }

    private Sort toSort(SortDto sortDto) {
        Sort sort = new Sort();
        BeanUtil.copyProperties(sortDto, sort);
        sort.setId(sortDto.getId());
        return sort;
    }


    private List<SortDto> toDto(List<Sort> sorts) {
        List<SortDto> sortDtos = new ArrayList<>();
        for (Sort sort : sorts) {
            SortDto sortDto = new SortDto();
            BeanUtil.copyProperties(sort, sortDto);
            sortDto.setId(sort.getId());
            sortDto.setCreateTime(sort.getCreateTime());
            SortLevelEnum sortLevelEnum = SortLevelEnum.getSortLevelEnumByCode(sortDto.getSortLevel());
            if (Objects.nonNull(sortLevelEnum)) {
                sortDto.setSortLevelName(sortLevelEnum.getName());
            }
            sortDtos.add(sortDto);
        }
        return sortDtos;
    }

}
