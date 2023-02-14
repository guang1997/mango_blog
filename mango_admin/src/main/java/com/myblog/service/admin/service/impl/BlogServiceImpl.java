package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.entity.BlogTag;
import com.myblog.service.admin.entity.Sort;
import com.myblog.service.admin.entity.Tag;
import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.base.exception.BusinessException;
import com.myblog.service.base.handler.es.entity.BlogEsDto;
import com.myblog.service.base.handler.es.impl.BlogEsOperateHandler;
import com.myblog.service.admin.mapper.BlogMapper;
import com.myblog.service.admin.mapper.BlogTagMapper;
import com.myblog.service.admin.mapper.SortMapper;
import com.myblog.service.admin.mapper.TagMapper;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.EsBulkBehaviorEnum;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.handler.es.EsOperateManager;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.config.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@Slf4j
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private SortMapper sortMapper;

    @Autowired
    private EsOperateManager esOperateManager;

    @PostConstruct
    private void init() throws Exception {
        // 将数据库中的博客保存到es中
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        List<Blog> dbBlogList = baseMapper.selectList(queryWrapper);
        List<BlogEsDto> esBlogList = dbBlogList.stream().map(this::toEsDto).collect(Collectors.toList());
        for (BlogEsDto blogEsDto : esBlogList) {
            BlogEsDto esBlogDto = esOperateManager.searchById(blogEsDto.getId(), BlogEsOperateHandler.class,
                BlogEsDto.class);
            if (Objects.nonNull(esBlogDto)) {
                continue;
            }
            boolean response = esOperateManager.bulk(esBlogList, EsBulkBehaviorEnum.INDEX, BlogEsOperateHandler.class);
            if (BooleanUtils.isFalse(response)) {
                log.error("init failed by blog insert es failed, dbBlogList:{}, esBlogList:{}", dbBlogList, esBlogList);
                throw new RuntimeException("标签插入es失败");
            }
        }
    }

    /**
     * 获取博客总数量，用于首页展示
     *
     * @return
     */
    @Override
    public int getBlogCount() {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 根据获取标签和博客数量，用于首页展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getBlogCountByTag() {
        List<Map<String, Object>> blogTagList = blogTagMapper.getBlogCountByTag();
        if (Objects.equals(blogTagList.size(), 0)) {
            return blogTagList;
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
                throw new BusinessException("getBlogCountByTag failed, cannot find tag from db by id " + blogTagMap.get("tagId"));
            }
            blogTagMap.put(Constants.ReplyField.NAME, blogTag.getTagName());
        }
        return blogTagList;
    }

    /**
     * 根据获取分类和博客数量，用于首页展示
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getBlogCountByBlogSort() {
        List<Map<String, Object>> blogSortList = baseMapper.getBlogCountByBlogSort();
        if (Objects.equals(blogSortList.size(), 0)) {
            return blogSortList;
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
                throw new BusinessException("getBlogCountByBlogSort failed, cannot find sort from db by id " + blogSortMap.get("sortId"));
            }
            blogSortMap.put(Constants.ReplyField.NAME, sort.getSortName());
        }
        return blogSortList;
    }

    /**
     * 获取一年内文章贡献度
     *
     * @return
     */
    @Override
    public Map<String, Object> getBlogContributeCount() throws Exception {
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
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put(Constants.ReplyField.CONTRIBUTE_DATE, contributeDateList);
        resultMap.put(Constants.ReplyField.BLOG_CONTRIBUTE_COUNT, resultList);
        return resultMap;
    }

    /**
     * 分页查询博客信息
     *
     * @param blogDto
     * @return
     */
    @Override
    public Map<String, Object> getBlogByPage(BlogDto blogDto) throws Exception {
        blogDto.setPage((blogDto.getPage() - 1) * blogDto.getSize());

        List<Blog> blogList = baseMapper.selectBlogByRequest(blogDto);
        // 数据总数，由于可能使用left join导致总条数不准，因此重新查一次
        int totalSize = baseMapper.selectBlogCountByRequest(blogDto);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constants.ReplyField.DATA, this.toDtoList(blogList, BlogDto.class));
        resultMap.put(Constants.ReplyField.TOTAL, totalSize);
        resultMap.put(Constants.ReplyField.PAGE, blogDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, blogDto.getSize());

        // 获取所有的标签
        List<Tag> tags = tagMapper.selectList(null);
        resultMap.put(Constants.ReplyField.BLOG_TAGS, tags);
        // 获取所有的分类
        List<Sort> sorts = sortMapper.selectList(null);
        resultMap.put(Constants.ReplyField.BLOG_SORTS, sorts);
        return resultMap;
    }

    /**
     * 添加博客
     *
     * @param blogDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBlog(BlogDto blogDto) throws Exception {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Blog.TITLE, blogDto.getTitle());
        if (baseMapper.selectList(queryWrapper).size() > 0) {
            throw new BusinessException("保存失败, 已存在相同名称的博客");
        }
        // 保存博客
        Blog blog = toDb(blogDto, Blog.class);
        if (baseMapper.insert(blog) < 1) {
            log.error("addBlog failed by unknown error, blog:{}", blog);
            return false;
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
                log.error("addBlog failed by add tags failed, tagId:{}, blog:{}", tagId, dbBlog);
                return false;
            }
        }
        // 保存数据到es
        boolean insertEsResponse = esOperateManager.insert(toEsDto(dbBlog), BlogEsOperateHandler.class);
        if (BooleanUtils.isFalse(insertEsResponse)) {
            log.error("insertEs failed, dbBlog:{}", dbBlog);
            throw new BusinessException("insertEs failed");
        }
        return true;
    }

    /**
     * 编辑博客
     *
     * @param blogDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editBlog(BlogDto blogDto) throws Exception {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Blog.TITLE, blogDto.getTitle());
        List<Blog> dbBlogList = baseMapper.selectList(queryWrapper);
        if (dbBlogList.size() > 0) {
            for (Blog blog : dbBlogList) {
                if (!Objects.equals(blog.getId(), blogDto.getId())) {
                    throw new BusinessException("更新失败, 已存在相同名称的博客");
                }
            }
        }

        // 保存博客
        Blog blog = toDb(blogDto, Blog.class);
        if (baseMapper.updateById(blog) < 1) {
            log.error("editBlog failed by unknown error, blog:{}", blog);
            return false;
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
                log.error("addBlog failed by add tags failed, tagId:{}, blog:{}", tagId, blog);
                return false;
            }
        }
        // 更新es的数据
        boolean updateEsResponse = esOperateManager.update(toEsDto(blog), BlogEsOperateHandler.class);
        if (BooleanUtils.isFalse(updateEsResponse)) {
            log.error("updateEs failed, dbBlog:{}", blog);
            throw new BusinessException("updateEs failed");
        }
        return true;
    }

    /**
     * 删除博客
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> delBlog(Set<String> ids) throws Exception {
        List<String> fileIdList = new ArrayList<>();
        for (String blogId : ids) {
            Blog dbBlog = baseMapper.selectById(blogId);
            if (Objects.isNull(dbBlog)) {
                log.error("delBlog failed, cannot find blog from db, blogId:{}", blogId);
                return null;
            }
            // 如果博客中的作者与当前登陆用户不一致不删除
            String currentUserId = SecurityUtils.getCurrentUserId();
            if (!Objects.equals(currentUserId, dbBlog.getAdminId())) {
                log.error("delBlog failed, current adminId:{} not equals db adminId:{}, blogId:{}", currentUserId, dbBlog.getAdminId(), blogId);
                return null;
            }
            // 删除绑定的菜单
            UpdateWrapper<BlogTag> blogTagUpdateWrapper = new UpdateWrapper<>();
            blogTagUpdateWrapper.eq(DbConstants.BlogTag.BLOG_ID, blogId);
            blogTagMapper.delete(blogTagUpdateWrapper);
            if (baseMapper.deleteById(blogId) < 1) {
                log.error("delBlog failed by unknown error, blogId:{}", blogId);
                return null;
            }
            fileIdList.add(dbBlog.getFileId());
        }
        List<BlogEsDto> delEsList = ids.stream().map(id -> {
            BlogEsDto blogEsDto = new BlogEsDto();
            blogEsDto.setId(id);
            return blogEsDto;
        }).collect(Collectors.toList());
        boolean esResponse = esOperateManager.bulk(delEsList, EsBulkBehaviorEnum.DELETE, BlogEsOperateHandler.class);
        if (BooleanUtils.isFalse(esResponse)) {
            log.error("delblog delete es failed, ids:{}, delEsList:{}", ids, delEsList);
            throw new BusinessException("delblog delete es failed");
        }
        return fileIdList;
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

    public BlogEsDto toEsDto(Blog blog) {
        BlogEsDto blogEsDto = new BlogEsDto();
        BeanUtil.copyProperties(blog, blogEsDto);
        blogEsDto.setId(blog.getId());
        return blogEsDto;
    }
}
