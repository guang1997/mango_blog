package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.entity.Sort;
import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.admin.mapper.BlogMapper;
import com.myblog.service.admin.mapper.SortMapper;
import com.myblog.service.admin.service.SortService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public Response getSortByPage(SortDto sortDto) throws Exception {
        Response response = Response.ok();
        if (Objects.nonNull(sortDto.getQueryAll()) && sortDto.getQueryAll()) {
            List<SortDto> sortDtos = this.toDtoList(baseMapper.selectList(null), SortDto.class);
            response.data(Constants.ReplyField.DATA, sortDtos);
            return response;
        }
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

        List<SortDto> sortDtos = this.toDtoList(sortPage.getRecords(), SortDto.class);
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
    public Response addSort(SortDto sortDto) throws Exception{
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            LOGGER.error("addSort failed, sortName is already exist, tag:{}", sortDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Sort sort = this.toDb(sortDto, Sort.class);
        if (baseMapper.insert(sort) < 1) {
            LOGGER.error("addSort failed by unknown error, sort:{}", sort);
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
    public Response editSort(SortDto sortDto) throws Exception{
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        List<Sort> sorts = baseMapper.selectList(queryWrapper);
        if (sorts.size() > 0) {
            for (Sort sort : sorts) {
                if (!Objects.equals(sort.getId(), sortDto.getId())) {
                    return Response.error().message("更新失败, 已存在相同名称的分类");
                }
            }
        }
        Sort sort = this.toDb(sortDto, Sort.class);
        if (baseMapper.updateById(sort) < 1) {
            LOGGER.error("editSort failed by unknown error, sort:{}", sort);
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
    public Response delSorts(Set<String> ids) throws Exception{
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
                LOGGER.error("delSorts failed by unknown error, sortId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        if (CollectionUtils.isEmpty(delFailedSortNames)) {
            return Response.ok();
        }
        return Response.error().message(delFailedSortNames.toString() + "已绑定用户, 未删除成功");
    }

}
