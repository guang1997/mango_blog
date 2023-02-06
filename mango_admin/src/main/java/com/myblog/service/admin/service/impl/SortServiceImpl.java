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
import com.myblog.service.base.exception.BusinessException;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
@Service
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements SortService {

    @Autowired
    private BlogMapper blogMapper;

    /**
     * 分页查询分类信息
     * @param sortDto
     * @return
     */
    @Override
    public Map<String, Object> getSortByPage(SortDto sortDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (Objects.nonNull(sortDto.getQueryAll()) && sortDto.getQueryAll()) {
            List<SortDto> sortDtos = this.toDtoList(baseMapper.selectList(null), SortDto.class);
            resultMap.put(Constants.ReplyField.DATA, sortDtos);
            return resultMap;
        }
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(sortDto.getSortName())) {
            queryWrapper.like(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        }
        if (!CollectionUtils.isEmpty(sortDto.getCreateTimes()) && Objects.equals(2, sortDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(sortDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(sortDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        Page<Sort> sortPage = new Page<>(sortDto.getPage(), sortDto.getSize());

        baseMapper.selectPage(sortPage, queryWrapper);

        List<SortDto> sortDtos = this.toDtoList(sortPage.getRecords(), SortDto.class);
        resultMap.put(Constants.ReplyField.DATA, sortDtos);
        resultMap.put(Constants.ReplyField.TOTAL, sortPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, sortDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, sortDto.getSize());
        return resultMap;
    }

    /**
     * 添加分类
     * @param sortDto
     * @return
     */
    @Override
    public Boolean addSort(SortDto sortDto) throws Exception{
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            log.error("addSort failed, sortName is already exist, tag:{}", sortDto);
            return false;
        }
        Sort sort = this.toDb(sortDto, Sort.class);
        if (baseMapper.insert(sort) < 1) {
            log.error("addSort failed by unknown error, sort:{}", sort);
            return false;
        }
        return true;
    }

    /**
     * 编辑分类
     * @param sortDto
     * @return
     */
    @Override
    public Boolean editSort(SortDto sortDto) throws Exception{
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Sort.SORT_NAME, sortDto.getSortName());
        List<Sort> sorts = baseMapper.selectList(queryWrapper);
        if (sorts.size() > 0) {
            for (Sort sort : sorts) {
                if (!Objects.equals(sort.getId(), sortDto.getId())) {
                    throw new BusinessException("更新失败, 已存在相同名称的分类");
                }
            }
        }
        Sort sort = this.toDb(sortDto, Sort.class);
        if (baseMapper.updateById(sort) < 1) {
            log.error("editSort failed by unknown error, sort:{}", sort);
            return false;
        }
        return true;
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delSorts(Set<String> ids) throws Exception{
        // 如果分类已经绑定了博客，那么不删除该分类
        List<String> delFailedSortNames = new ArrayList<>();
        for (String id : ids) {
            QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
            blogQueryWrapper.eq(DbConstants.Blog.BLOG_SORT_ID, id);
            List<Blog> blogsBySortId = blogMapper.selectList(blogQueryWrapper);
            if (!CollectionUtils.isEmpty(blogsBySortId)) {
                Sort sort = baseMapper.selectById(id);
                log.warn("delete sort failed, the sort:{} has been bound", sort);
                delFailedSortNames.add(sort.getSortName());
                continue;
            }
            if (baseMapper.deleteById(id) < 1) {
                log.error("delSorts failed by unknown error, sortId:{}", id);
                return false;
            }
        }
        if (CollectionUtils.isEmpty(delFailedSortNames)) {
            return true;
        }
        throw new BusinessException(delFailedSortNames + "已绑定博客, 未删除成功");
    }

}
