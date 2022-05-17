package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.entity.BlogTag;
import com.myblog.service.admin.entity.Sort;
import com.myblog.service.admin.entity.Tag;
import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.mapper.BlogMapper;
import com.myblog.service.admin.mapper.BlogTagMapper;
import com.myblog.service.admin.mapper.SortMapper;
import com.myblog.service.admin.mapper.TagMapper;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.config.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    private static Logger LOGGER = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private SortMapper sortMapper;

    /**
     * 获取博客总数量，用于首页展示
     *
     * @return
     */
    @Override
    public int getBlogCount() {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 根据获取标签和博客数量，用于首页展示
     *
     * @return
     */
    @Override
    public Response getBlogCountByTag() {
        Response response = Response.ok();
        List<Map<String, Object>> blogTagList = blogTagMapper.getBlogCountByTag();
        if (Objects.equals(blogTagList.size(), 0)) {
            response.data(Constants.ReplyField.DATA, blogTagList);
            return response;
        }
        Set<String> tagIds = blogTagList.stream()
                .map(map -> map.get("tagId").toString())
                .collect(Collectors.toSet());
        List<Tag> tags = tagMapper.selectBatchIds(tagIds);
        for (Map<String, Object> blogTagMap : blogTagList) {
            Tag blogTag = tags.stream()
                    .filter(tag -> Objects.equals(tag.getId(), blogTagMap.get("tagId")))
                    .findAny().orElse(null);
            if (Objects.isNull(blogTag)) {
                LOGGER.error("getBlogCountByTag failed, cannot find tag from db by id:{}", blogTagMap.get("tagId"));
                response.setCode(ResultCodeEnum.QUERY_FAILED.getCode());
                response.setMessage(ResultCodeEnum.QUERY_FAILED.getMessage());
                return response;
            }
            blogTagMap.put(Constants.ReplyField.NAME, blogTag.getTagName());
        }
        response.data(Constants.ReplyField.DATA, blogTagList);
        return response;
    }

    /**
     * 根据获取分类和博客数量，用于首页展示
     *
     * @return
     */
    @Override
    public Response getBlogCountByBlogSort() {
        Response response = Response.ok();
        List<Map<String, Object>> blogSortList = baseMapper.getBlogCountByBlogSort();
        if (Objects.equals(blogSortList.size(), 0)) {
            response.data(Constants.ReplyField.DATA, blogSortList);
            return response;
        }
        Set<String> sortIds = blogSortList.stream()
                .map(map -> map.get("sortId").toString())
                .collect(Collectors.toSet());
        List<Sort> sorts = sortMapper.selectBatchIds(sortIds);
        for (Map<String, Object> blogSortMap : blogSortList) {
            Sort sort = sorts.stream()
                    .filter(tag -> Objects.equals(tag.getId(), blogSortMap.get("sortId")))
                    .findAny().orElse(null);
            if (Objects.isNull(sort)) {
                LOGGER.error("getBlogCountByBlogSort failed, cannot find sort from db by id:{}", blogSortMap.get("sortId"));
                response.setCode(ResultCodeEnum.QUERY_FAILED.getCode());
                response.setMessage(ResultCodeEnum.QUERY_FAILED.getMessage());
                return response;
            }
            blogSortMap.put(Constants.ReplyField.NAME, sort.getSortName());
        }
        response.data(Constants.ReplyField.DATA, blogSortList);
        return response;
    }

    /**
     * 获取一年内文章贡献度
     *
     * @return
     */
    @Override
    public Response getBlogContributeCount() throws Exception {
        Response response = Response.ok();
        // 获取当天结束时间
        String endTime = ThreadSafeDateFormat.getTodayEndTime();
        // 获取365天之前的日期
        String startTime = ThreadSafeDateFormat.getDate(endTime, -364);

        List<Map<String, Object>> blogCountByDateList = baseMapper.getBlogContributeCount(startTime, endTime);

        Map<String, Object> blogCountByDateMap = blogCountByDateList.stream()
                .collect(Collectors.toMap(item -> item.get("date").toString(), item -> item.get("count"), (item1, item2) -> item1));
        List<String> dateList = ThreadSafeDateFormat.getDayBetweenDates(startTime, endTime);

        List<List<Object>> resultList = new ArrayList<>();
        for (String date : dateList) {
            int count = 0;
            if (Objects.nonNull(blogCountByDateMap.get(date))) {
                count = Integer.parseInt(blogCountByDateMap.get(date).toString());
            }
            List<Object> countList = new ArrayList<>();
            countList.add(date);
            countList.add(count);
            resultList.add(countList);
        }
        List<String> contributeDateList = new ArrayList<>();
        contributeDateList.add(startTime);
        contributeDateList.add(endTime);
        response.data(Constants.ReplyField.CONTRIBUTE_DATE, contributeDateList);
        response.data(Constants.ReplyField.BLOG_CONTRIBUTE_COUNT, resultList);
        return response;
    }

    /**
     * 分页查询博客信息
     *
     * @param blogDto
     * @return
     */
    @Override
    public Response getBlogByPage(BlogDto blogDto) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 10;
        if (Objects.nonNull(blogDto.getPage())) page = blogDto.getPage();
        if (Objects.nonNull(blogDto.getSize())) size = blogDto.getSize();
        blogDto.setPage((blogDto.getPage() - 1) * blogDto.getSize());

        List<Blog> blogList = baseMapper.selectBlogByRequest(blogDto);
        // 数据总数，由于可能使用left join导致总条数不准，因此重新查一次
        int totalSize = baseMapper.selectBlogCountByRequest(blogDto);
        response.data(Constants.ReplyField.DATA, this.toDtoList(blogList, BlogDto.class));
        response.data(Constants.ReplyField.TOTAL, totalSize);
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);

        // 获取所有的标签
        List<Tag> tags = tagMapper.selectList(null);
        response.data(Constants.ReplyField.BLOG_TAGS, tags);
        // 获取所有的分类
        List<Sort> sorts = sortMapper.selectList(null);
        response.data(Constants.ReplyField.BLOG_SORTS, sorts);
        return response;
    }

    /**
     * 添加博客
     *
     * @param blogDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addBlog(BlogDto blogDto) throws Exception {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Blog.TITLE, blogDto.getTitle());
        if (baseMapper.selectList(queryWrapper).size() > 0) {
            return Response.error().message("保存失败, 已存在相同名称的博客");
        }
        // 保存博客
        Blog blog = toDb(blogDto, Blog.class);
        if (baseMapper.insert(blog) < 1) {
            LOGGER.error("addBlog failed by unknown error, blog:{}", blog);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        // 查询已经保存的博客
        Blog dbBlog = baseMapper.selectOne(queryWrapper);
        // 保存标签
        List<String> tagIds = blogDto.getTags().stream().map(Tag::getId).distinct().collect(Collectors.toList());
        for (String tagId : tagIds) {
            BlogTag blogTag = new BlogTag();
            blogTag.setBlogId(dbBlog.getId());
            blogTag.setTagId(tagId);
            if (blogTagMapper.insert(blogTag) < 1) {
                LOGGER.error("addBlog failed by add tags failed, tagId:{}, blog:{}", tagId, dbBlog);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }
        return Response.ok();
    }

    /**
     * 编辑博客
     *
     * @param blogDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response editBlog(BlogDto blogDto) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Blog.TITLE, blogDto.getTitle());
        List<Blog> dbBlogList = baseMapper.selectList(queryWrapper);
        if (dbBlogList.size() > 0) {
            for (Blog blog : dbBlogList) {
                if (!Objects.equals(blog.getId(), blogDto.getId())) {
                    return Response.error().message("更新失败, 已存在相同名称的博客");
                }
            }
        }
        // 保存博客
        Blog blog = toDb(blogDto, Blog.class);
        if (baseMapper.updateById(blog) < 1) {
            LOGGER.error("editBlog failed by unknown error, blog:{}", blog);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 保存标签
        List<String> tagIds = blogDto.getTags().stream().map(Tag::getId).distinct().collect(Collectors.toList());
        UpdateWrapper<BlogTag> blogTagUpdateWrapper = new UpdateWrapper<>();
        blogTagUpdateWrapper.eq(DbConstants.BlogTag.BLOG_ID, blog.getId());
        blogTagMapper.delete(blogTagUpdateWrapper);
        for (String tagId : tagIds) {
            BlogTag blogTag = new BlogTag();
            blogTag.setBlogId(blog.getId());
            blogTag.setTagId(tagId);
            if (blogTagMapper.insert(blogTag) < 1) {
                LOGGER.error("addBlog failed by add tags failed, tagId:{}, blog:{}", tagId, blog);
                return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
            }
        }
        return Response.ok();
    }

    /**
     * 删除博客
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response delBlog(Set<String> ids) {
        for (String blogId : ids) {
            // 如果博客中的作者与当前登陆用户不一致不删除
            Blog dbBlog = baseMapper.selectById(blogId);
            if (Objects.isNull(dbBlog)) {
                LOGGER.error("delBlog failed, cannot find blog from db, blogId:{}", blogId);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
            String currentUserId = SecurityUtils.getCurrentUserId();
            if (!Objects.equals(currentUserId, dbBlog.getAdminId())) {
                LOGGER.error("delBlog failed, current adminId:{} not equals db adminId:{}, blogId:{}", currentUserId, dbBlog.getAdminId(), blogId);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
            // 删除绑定的菜单
            UpdateWrapper<BlogTag> blogTagUpdateWrapper = new UpdateWrapper<>();
            blogTagUpdateWrapper.eq(DbConstants.BlogTag.BLOG_ID, blogId);
            blogTagMapper.delete(blogTagUpdateWrapper);
            if (baseMapper.deleteById(blogId) < 1) {
                LOGGER.error("delBlog failed by unknown error, blogId:{}", blogId);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        return Response.ok();
    }

    @Override
    public void setDtoExtraProperties(Blog db, BlogDto dto) {
        dto.setId(db.getId());
        dto.setCreateTime(db.getCreateTime());
        dto.setUpdateTime(db.getUpdateTime());
        Sort blogSort = sortMapper.selectById(db.getBlogSortId());
        if (Objects.nonNull(blogSort)) {
            dto.setSortName(blogSort.getSortName());
        }
        QueryWrapper<BlogTag> blogTagQueryWrapper = new QueryWrapper<>();
        blogTagQueryWrapper.eq(DbConstants.BlogTag.BLOG_ID, db.getId());
        List<BlogTag> blogTags = blogTagMapper.selectList(blogTagQueryWrapper);
        if (!CollectionUtils.isEmpty(blogTags)) {
            Set<String> tagIds = blogTags.stream().map(BlogTag::getTagId).collect(Collectors.toSet());
            List<Tag> tags = tagMapper.selectBatchIds(tagIds);
            if (!CollectionUtils.isEmpty(tags)) {
                dto.setTags(tags);
            }
        }
    }

    @Override
    public Blog toDb(BlogDto dto, Class<Blog> dbClazz) {
        Blog blog = new Blog();
        BeanUtil.copyProperties(dto, blog);
        setDbExtraProperties(blog, dto);
        // 设置用户信息
        blog.setAdminId(SecurityUtils.getCurrentUserId());
        blog.setAuthor(SecurityUtils.getCurrentUsername());
        return blog;
    }
}
