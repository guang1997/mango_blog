package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.Sort;
import com.myblog.service.web.entity.dto.SortDto;
import com.myblog.service.web.mapper.SortMapper;
import com.myblog.service.web.service.SortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private static Logger LOGGER = LoggerFactory.getLogger(SortServiceImpl.class);

    @Override
    public Response getSortByPage(SortDto blogDto) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 5;
        if (Objects.nonNull(blogDto.getPage())) page = blogDto.getPage();
        if (Objects.nonNull(blogDto.getSize())) size = blogDto.getSize();

        Page<Sort> sortPage = new Page<>(page, size);
        QueryWrapper<Sort> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(sortPage, queryWrapper);

        response.data(Constants.ReplyField.DATA, this.toDtoList(sortPage.getRecords(), SortDto.class));
        response.data(Constants.ReplyField.TOTAL, sortPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }
}
