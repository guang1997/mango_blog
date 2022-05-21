package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.Sort;
import com.myblog.service.web.entity.dto.SortDto;
import com.myblog.service.web.mapper.BlogMapper;
import com.myblog.service.web.mapper.SortMapper;
import com.myblog.service.web.service.SortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@Service
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements SortService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Response getSortByPage(SortDto sortDto) throws Exception {
        Response response = Response.ok();
        if (Objects.nonNull(sortDto.getQueryAll()) && sortDto.getQueryAll()) {
            List<SortDto> sortDtos = this.toDtoList(baseMapper.selectList(null), SortDto.class);
            response.data(Constants.ReplyField.DATA, sortDtos);
            response.data(Constants.ReplyField.TOTAL, sortDtos.size());
            return response;
        }
        int page = 1;
        int size = 5;
        if (Objects.nonNull(sortDto.getPage())) page = sortDto.getPage();
        if (Objects.nonNull(sortDto.getSize())) size = sortDto.getSize();

        Page<Sort> sortPage = new Page<>(page, size);
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(sortPage, queryWrapper);
        List<SortDto> sortDtoList = this.toDtoList(sortPage.getRecords(), SortDto.class);
        // 查询分类绑定的博客数量
        List<Map<String, Object>> blogSortNumList = blogMapper.selectCountGroupByBlogSort();
        for (Map<String, Object> blogSortNumMap : blogSortNumList) {
            SortDto dbSortDto = sortDtoList.stream()
                    .filter(dto -> Objects.equals(dto.getId(), blogSortNumMap.get(DbConstants.Blog.BLOG_SORT_ID)))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(dbSortDto) && Objects.nonNull(blogSortNumMap.get("blogNum"))) {
                dbSortDto.setBlogNum(Integer.parseInt(blogSortNumMap.get("blogNum").toString()));
            }
        }
        response.data(Constants.ReplyField.DATA, sortDtoList);
        response.data(Constants.ReplyField.TOTAL, sortPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    public void setDtoExtraProperties(Sort db, SortDto dto) {
        dto.setId(db.getId());
        dto.setCreateTime(db.getCreateTime());
        dto.setUpdateTime(db.getUpdateTime());
        dto.setBlogNum(0);
    }
}
