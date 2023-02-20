package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.*;
import com.myblog.service.base.common.Constants.ReplyField;
import com.myblog.service.base.exception.BusinessException;
import com.myblog.service.base.handler.es.EsOperateManager;
import com.myblog.service.base.handler.es.entity.BlogEsDto;
import com.myblog.service.base.handler.es.impl.BlogEsOperateHandler;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.MD5Utils;
import com.myblog.service.base.util.PageUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.web.controller.BlogController;
import com.myblog.service.web.entity.*;
import com.myblog.service.web.entity.dto.ArchiveDto;
import com.myblog.service.web.entity.dto.BlogDto;
import com.myblog.service.web.mapper.*;
import com.myblog.service.web.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.web.util.UniqueKeyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
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

    @Autowired
    private EsOperateManager esOperateManager;

    /**
     * 分页查询博客信息
     *
     * @param blogDto
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getBlogByPage(BlogDto blogDto) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        blogDto.setPage((blogDto.getPage() - 1) * blogDto.getSize());

        List<Blog> blogList = baseMapper.selectBlogByRequest(blogDto);
        // 数据总数，由于可能使用left join导致总条数不准，因此重新查一次
        int totalSize = baseMapper.selectBlogCountByRequest(blogDto);

        resultMap.put(Constants.ReplyField.DATA, this.toDtoList(blogList, BlogDto.class));
        resultMap.put(Constants.ReplyField.TOTAL, totalSize);
        resultMap.put(Constants.ReplyField.PAGE, blogDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, blogDto.getSize());
        return resultMap;
    }

    /**
     * 根据博客分类查询博客信息
     *
     * @param blogDto
     * @return
     */
    @Override
    public List<BlogDto> getBlogBySortId(BlogDto blogDto) throws Exception {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        queryWrapper.eq(DbConstants.Blog.BLOG_SORT_ID, blogDto.getBlogSortId());
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        List<Blog> blogs = baseMapper.selectList(queryWrapper);

        // 查询标签信息
        List<BlogDto> blogDtos = this.toDtoList(blogs, BlogDto.class);
        for (BlogDto dto : blogDtos) {
            dto.setTags(this.getBlogTags(blogDto.getId()));
        }

        return blogDtos;
    }

    /**
     * 根据博客标签查询博客信息
     *
     * @param blogDto
     * @return
     */
    @Override
    public List<BlogDto> getBlogByTagId(BlogDto blogDto) throws Exception {
        QueryWrapper<BlogTag> blogTagQueryWrapper = new QueryWrapper<>();
        blogTagQueryWrapper.eq(DbConstants.BlogTag.TAG_ID, blogDto.getTagId());
        List<BlogTag> blogTags = blogTagMapper.selectList(blogTagQueryWrapper);
        if (CollectionUtils.isEmpty(blogTags)) {
            return new ArrayList<>();
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
        return blogDtos;
    }

    /**
     * 根据博客id获取博客信息
     *
     * @param blogDto
     * @return
     */
    @Override
    public BlogDto getBlogById(BlogDto blogDto, HttpServletRequest request) throws Exception {
        Blog blog = baseMapper.selectById(blogDto.getId());
        if (Objects.isNull(blog)) {
            LOGGER.error("cannot find blog by id:{}", blogDto.getId());
            throw new BusinessException("未找到博客");
        }
        BlogDto responseDto = this.toDto(blog, BlogDto.class);
        // 查询该ip是否已经给博客点过赞
        responseDto.setLiked(getBlogLiked(responseDto.getId(),
                IpUtils.getIpAddr(request),
                blogDto.getUserId(),
                MD5Utils.string2MD5(UniqueKeyUtil.getUniqueKey(request, blogDto.getScreenInformation())),
                blogDto.getBrowserFinger()));
        // 博客点击量+1
        blog.setClickCount(blog.getClickCount() + 1);
        baseMapper.updateById(blog);
        return responseDto;
    }

    /**
     * 获取上一篇和下一篇博客
     *
     * @param blogDto
     * @return
     */
    @Override
    public Map<String, Object> getPrevNextBlog(BlogDto blogDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        // 获取上一篇博客
        QueryWrapper<Blog> prevQueryWrapper = new QueryWrapper<>();
        prevQueryWrapper.lt(DbConstants.Base.CREATE_TIME, blogDto.getCreateTime());
        prevQueryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        prevQueryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        List<Blog> prevBlogList = baseMapper.selectList(prevQueryWrapper);
        if (!CollectionUtils.isEmpty(prevBlogList)) {
            resultMap.put(Constants.ReplyField.PREV_BLOG, this.toDto(prevBlogList.get(0), BlogDto.class));
        }
        // 获取下一篇博客
        QueryWrapper<Blog> nextQueryWrapper = new QueryWrapper<>();
        nextQueryWrapper.gt(DbConstants.Base.CREATE_TIME, blogDto.getCreateTime());
        prevQueryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        prevQueryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        List<Blog> nextBlogList = baseMapper.selectList(nextQueryWrapper);
        if (!CollectionUtils.isEmpty(nextBlogList)) {
            resultMap.put(Constants.ReplyField.NEXT_BLOG, this.toDto(nextBlogList.get(0), BlogDto.class));
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getArchives(ArchiveDto archiveDto) {
        Map<String, Object> resultMap = new HashMap<>();
        Page<Blog> archivePage = new Page<>(archiveDto.getPage(), archiveDto.getSize());
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        if (StringUtils.isNotBlank(archiveDto.getMonth())) {
            queryWrapper.apply("date_format(create_time, '%Y-%m') = {0}", archiveDto.getMonth());
        }
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(archivePage, queryWrapper);
        if (CollectionUtils.isEmpty(archivePage.getRecords())) {
            resultMap.put(Constants.ReplyField.DATA, new ArrayList<>());
        } else {
            Map<String, ArchiveDto> responseMap = new LinkedHashMap<>();
            for (Blog blog : archivePage.getRecords()) {
                String year = ThreadSafeDateFormat.format(blog.getCreateTime(), ThreadSafeDateFormat.YEAR);
                ArchiveDto yearDto = new ArchiveDto();
                if (Objects.nonNull(responseMap.get(year))) {
                    yearDto = responseMap.get(year);
                }
                if (BooleanUtils.isTrue(archiveDto.getQueryByMonth())) {
                    yearDto.setMonth(ThreadSafeDateFormat.format(blog.getCreateTime(), ThreadSafeDateFormat.MONTH));
                }
                yearDto.setYear(year);
                yearDto.setQueryByMonth(archiveDto.getQueryByMonth());
                yearDto.getChildrens().add(toArchiveDto(blog));
                responseMap.put(year, yearDto);
            }
            resultMap.put(Constants.ReplyField.DATA, responseMap.values());
        }
        resultMap.put(Constants.ReplyField.TOTAL, archivePage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, archiveDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, archiveDto.getSize());
        return resultMap;
    }

    @Override
    public List<ArchiveDto> initArchives(ArchiveDto archiveDto) {
        if (BooleanUtils.isTrue(archiveDto.getQueryByMonth()) && StringUtils.isBlank(archiveDto.getMonth())) {
            List<ArchiveDto> archiveDtos = baseMapper.selectBlogNumByMouth();
            return archiveDtos;
        }
        LOGGER.error("initArchives failed by illegal param:{}", archiveDto);
        throw new BusinessException("初始化归档信息失败");
    }

    @Override
    public Map<String, Object> getBlogByKeyword(BlogDto blogDto) throws Exception {
        if (StringUtils.isBlank(blogDto.getContent())) {
            throw new BusinessException("请输入查询条件");
        }
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.EsContants.BLOG_CONTENT, blogDto.getContent());
        List<BlogEsDto> esDtos = esOperateManager.search(param, BlogEsOperateHandler.class, BlogEsDto.class);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(ReplyField.DATA, PageUtil.page(esDtos, blogDto.getPage(), blogDto.getSize()));
        resultMap.put(ReplyField.PAGE, blogDto.getPage());
        resultMap.put(ReplyField.SIZE, blogDto.getSize());
        resultMap.put(ReplyField.TOTAL, esDtos.size());
        return resultMap;
    }

    private ArchiveDto toArchiveDto(Blog blog) {
        ArchiveDto archiveDto = new ArchiveDto();
        BeanUtil.copyProperties(blog, archiveDto);
        archiveDto.setMonth(ThreadSafeDateFormat.format(blog.getCreateTime(), ThreadSafeDateFormat.MONTH));
        archiveDto.setCreateTime(blog.getCreateTime());
        archiveDto.setId(blog.getId());
        return archiveDto;
    }

    private Boolean getBlogLiked(String id, String ipAddr, String userId, String uniqueKey, String browserFinger) {
        if (StringUtils.isNotBlank(userId)) {
            QueryWrapper<Comment> userIdQueryWrapper = new QueryWrapper<>();
            userIdQueryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommentStatus.REVIEWED);
            userIdQueryWrapper.eq(DbConstants.Comment.BLOG_ID, id);
            userIdQueryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
            userIdQueryWrapper.eq(DbConstants.Comment.SOURCE, CommentSourceEnum.BLOG_INFO_LIKES.getSource());
            userIdQueryWrapper.eq(DbConstants.Comment.USER_ID, userId);

            Comment comment = commentMapper.selectOne(userIdQueryWrapper);
            if (Objects.isNull(comment)) {
                return false;
            }
            return Objects.equals("true", comment.getContent());
        } else {
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommentStatus.REVIEWED);
            queryWrapper.eq(DbConstants.Comment.BLOG_ID, id);
            queryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
            queryWrapper.eq(DbConstants.Comment.SOURCE, CommentSourceEnum.BLOG_INFO_LIKES.getSource());
            queryWrapper.isNull(DbConstants.Comment.USER_ID);
            queryWrapper.and(wrapper -> wrapper
                    .and(wrapper2 -> wrapper2.eq(DbConstants.Comment.IP, ipAddr).eq(DbConstants.Comment.UNIQUE_KEY, uniqueKey))
                    .or().eq(DbConstants.Comment.BROWSER_FINGER, browserFinger));
            List<Comment> comments = commentMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(comments)) {
                return false;
            }
            for (Comment comment : comments) {
                if (Objects.equals("true", comment.getContent())) {
                    return true;
                }
            }
        }
        return false;
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
