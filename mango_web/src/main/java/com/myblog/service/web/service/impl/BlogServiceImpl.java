package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.Blog;
import com.myblog.service.web.entity.dto.BlogDto;
import com.myblog.service.web.mapper.BlogMapper;
import com.myblog.service.web.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-17
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Override
    public Response getBlogByPage(BlogDto blogDto) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 6;
        if (Objects.nonNull(blogDto.getPage())) page = blogDto.getPage();
        if (Objects.nonNull(blogDto.getSize())) size = blogDto.getSize();

        Page<Blog> blogPage = new Page<>(page, size);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(blogPage, queryWrapper);

        response.data(Constants.ReplyField.DATA, this.toDtoList(blogPage.getRecords(), BlogDto.class));
        response.data(Constants.ReplyField.TOTAL, blogPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }
}
