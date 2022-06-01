package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.*;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.MD5Utils;
import com.myblog.service.web.controller.BlogController;
import com.myblog.service.web.entity.*;
import com.myblog.service.web.entity.dto.BlogDto;
import com.myblog.service.web.mapper.*;
import com.myblog.service.web.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.web.util.UniqueKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
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
     *
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
     *
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
     *
     * @param blogDto
     * @return
     */
    @Override
    public Response getBlogByTagId(BlogDto blogDto) throws Exception {
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
     *
     * @param blogDto
     * @return
     */
    @Override
    public Response getBlogById(BlogDto blogDto, HttpServletRequest request) throws Exception {
        Response response = Response.ok();
        Blog blog = baseMapper.selectById(blogDto.getId());
        if (Objects.isNull(blog)) {
            LOGGER.error("cannot find blog by id:{}", blogDto.getId());
            return Response.setResult(ResultCodeEnum.QUERY_FAILED);
        }
        BlogDto responseDto = this.toDto(blog, BlogDto.class);
        // 查询该ip是否已经给博客点过赞
        responseDto.setLiked(getBlogLiked(responseDto.getId(),
                IpUtils.getIpAddr(request),
                blogDto.getUserId(),
                MD5Utils.string2MD5(UniqueKeyUtil.getUniqueKey(request, blogDto.getScreenInformation()))));
        response.data(Constants.ReplyField.DATA, responseDto);
        return response;
    }

    /**
     * 获取上一篇和下一篇博客
     *
     * @param blogDto
     * @return
     */
    @Override
    public Response getPrevNextBlog(BlogDto blogDto) throws Exception {
        Response response = Response.ok();
        // 获取上一篇博客
        QueryWrapper<Blog> prevQueryWrapper = new QueryWrapper<>();
        prevQueryWrapper.lt(DbConstants.Base.CREATE_TIME, blogDto.getCreateTime());
        prevQueryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        prevQueryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        List<Blog> prevBlogList = baseMapper.selectList(prevQueryWrapper);
        if (!CollectionUtils.isEmpty(prevBlogList)) {
            response.data(Constants.ReplyField.PREV_BLOG, this.toDto(prevBlogList.get(0), BlogDto.class));
        }
        // 获取下一篇博客
        QueryWrapper<Blog> nextQueryWrapper = new QueryWrapper<>();
        nextQueryWrapper.gt(DbConstants.Base.CREATE_TIME, blogDto.getCreateTime());
        prevQueryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        prevQueryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        List<Blog> nextBlogList = baseMapper.selectList(nextQueryWrapper);
        if (!CollectionUtils.isEmpty(nextBlogList)) {
            response.data(Constants.ReplyField.NEXT_BLOG, this.toDto(nextBlogList.get(0), BlogDto.class));
        }
        return response;
    }

    private Boolean getBlogLiked(String id, String ipAddr, String userId, String uniqueKey) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommentStatus.REVIEWED);
        queryWrapper.eq(DbConstants.Comment.BLOG_ID, id);
        queryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
        queryWrapper.eq(DbConstants.Comment.SOURCE, CommentSourceEnum.BLOG_INFO_LIKES.getSource());
        queryWrapper.and(wrapper -> wrapper
                .and(wrapper2 -> wrapper2.eq(DbConstants.Comment.IP, ipAddr).eq(DbConstants.Comment.UNIQUE_KEY, uniqueKey))
                .or().eq(DbConstants.Comment.USER_ID, userId));


        Comment comment = commentMapper.selectOne(queryWrapper);
        if (Objects.isNull(comment)) {
            return false;
        }
        return !Objects.equals("false", comment.getContent());
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
//        dto.setComments(getBlogComments(db.getId()));
    }

    private List<Comment> getBlogComments(String id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Comment.STATUS, 1);
        queryWrapper.eq(DbConstants.Comment.BLOG_ID, id);
        queryWrapper.ne(DbConstants.Comment.SOURCE, CommentSourceEnum.BLOG_INFO_LIKES);
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
