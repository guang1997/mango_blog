package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.web.controller.BlogController;
import com.myblog.service.web.entity.*;
import com.myblog.service.web.entity.dto.BlogDto;
import com.myblog.service.web.mapper.*;
import com.myblog.service.web.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static Logger LOGGER = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private SortMapper sortMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 分页查询博客信息
     * @param blogDto
     * @return
     * @throws Exception
     */
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

    /**
     * 根据博客分类查询博客信息
     * @param blogDto
     * @return
     */
    @Override
    public Response getBlogBySortId(BlogDto blogDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        queryWrapper.eq(DbConstants.Blog.BLOG_SORT_ID, blogDto.getBlogSortId());
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        List<Blog> blogs = baseMapper.selectList(queryWrapper);

        // 查询标签信息
        List<BlogDto> blogDtos = this.toDtoList(blogs, BlogDto.class);
        for (BlogDto dto : blogDtos) {
            dto.setTags(this.getBlogTags(blogDto.getId()));
        }

        response.data(Constants.ReplyField.DATA, blogDtos);
        return response;
    }

    /**
     * 根据博客标签查询博客信息
     * @param blogDto
     * @return
     */
    @Override
    public Response getBlogByTagId(BlogDto blogDto) throws Exception{
        Response response = Response.ok();
        QueryWrapper<BlogTag> blogTagQueryWrapper = new QueryWrapper<>();
        blogTagQueryWrapper.eq(DbConstants.BlogTag.TAG_ID, blogDto.getTagId());
        List<BlogTag> blogTags = blogTagMapper.selectList(blogTagQueryWrapper);
        if (CollectionUtils.isEmpty(blogTags)) {
            return response;
        }

        List<String> blogIds = blogTags.stream().map(BlogTag::getBlogId).distinct().collect(Collectors.toList());
        List<Blog> blogs = baseMapper.selectBatchIds(blogIds);
        List<BlogDto> blogDtos = this.toDtoList(blogs, BlogDto.class);
        for (BlogDto dto : blogDtos) {
            Sort blogSort = sortMapper.selectById(dto.getBlogSortId());
            if (Objects.nonNull(blogSort)) {
                dto.setSort(blogSort);
            }
        }
        response.data(Constants.ReplyField.DATA, blogDtos);
        return response;
    }

    /**
     * 根据博客id获取博客信息
     * @param blogDto
     * @return
     */
    @Override
    public Response getBlogById(BlogDto blogDto) throws Exception{
        Response response = Response.ok();
        Blog blog = baseMapper.selectById(blogDto.getId());
        if (Objects.isNull(blog)) {
            LOGGER.error("cannot find blog by id:{}", blogDto.getId());
            return Response.setResult(ResultCodeEnum.QUERY_FAILED);
        }
        BlogDto responseDto = this.toDto(blog, BlogDto.class);
        response.data(Constants.ReplyField.DATA, responseDto);
        return response;
    }

    @Override
    public BlogDto toDto(Blog db, Class<BlogDto> dtoClazz) throws IllegalAccessException, InstantiationException {
        BlogDto blogDto = dtoClazz.newInstance();
        BeanUtil.copyProperties(db, blogDto);
        this.setDtoExtraProperties(db, blogDto);
        // 查询标签信息
        blogDto.setTags(this.getBlogTags(blogDto.getId()));
        return blogDto;
    }

    @Override
    public void setDtoExtraProperties(Blog db, BlogDto dto) {
        dto.setId(db.getId());
        dto.setCreateTime(db.getCreateTime());
        dto.setUpdateTime(db.getUpdateTime());
        dto.setComments(getBlogComments(db.getId()));
    }

    private List<Comment> getBlogComments(String id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Comment.STATUS, 1);
        queryWrapper.eq(DbConstants.Comment.BLOG_ID, id);
        return commentMapper.selectList(queryWrapper);
    }

    private List<Tag> getBlogTags(String blogId) {
        List<Tag> tags = new ArrayList<>();
        QueryWrapper<BlogTag> blogTagQueryWrapper = new QueryWrapper<>();
        blogTagQueryWrapper.eq(DbConstants.BlogTag.BLOG_ID, blogId);
        List<BlogTag> blogTags = blogTagMapper.selectList(blogTagQueryWrapper);
        if (!CollectionUtils.isEmpty(blogTags)) {
            Set<String> tagIds = blogTags.stream().map(BlogTag::getTagId).collect(Collectors.toSet());
            tags = tagMapper.selectBatchIds(tagIds);
        }
        return tags;
    }
}
