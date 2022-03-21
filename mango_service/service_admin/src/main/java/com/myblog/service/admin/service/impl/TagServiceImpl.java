package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.admin.controller.TagController;
import com.myblog.service.admin.entity.BlogTag;
import com.myblog.service.admin.entity.Tag;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.mapper.BlogTagMapper;
import com.myblog.service.admin.mapper.TagMapper;
import com.myblog.service.admin.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private static Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private BlogTagMapper blogTagMapper;

    /**
     * 分页查询标签信息
     * @param tagDto
     * @return
     * @throws ParseException
     */
    @Override
    public Response getTagByPage(TagDto tagDto) throws ParseException {
        Response response = Response.ok();
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(tagDto.getPage())) page = tagDto.getPage();
        if (Objects.nonNull(tagDto.getSize())) size = tagDto.getSize();

        if (StringUtils.isNotBlank(tagDto.getTagName())) {
            queryWrapper.like(DbConstants.Tag.TAG_NAME, tagDto.getTagName());
        }
        if (!CollectionUtils.isEmpty(tagDto.getCreateTimes()) && Objects.equals(2, tagDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(tagDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(tagDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        queryWrapper.orderByDesc(DbConstants.Base.SORT);
        Page<Tag> tagPage = new Page<>(page, size);

        baseMapper.selectPage(tagPage, queryWrapper);

        List<TagDto> tagDtos = toDto(tagPage.getRecords());
        response.data(Constants.ReplyField.DATA, tagDtos);
        response.data(Constants.ReplyField.TOTAL, tagPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 添加标签
     * @param tagDto
     * @return
     */
    @Override
    public Response addTag(TagDto tagDto) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Tag.TAG_NAME, tagDto.getTagName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            LOGGER.error("addTag failed, tagName is already exist, tag:{}", tagDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Tag tag = toTag(tagDto);
        if (baseMapper.insert(tag) < 1) {
            LOGGER.error("addTag failed by unknown error, tag:{}", tagDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 修改标签
     * @param tagDto
     * @return
     */
    @Override
    public Response editTag(TagDto tagDto) {
        Tag tag = toTag(tagDto);
        if (baseMapper.updateById(tag) < 1) {
            LOGGER.error("editTag failed by unknown error, tag:{}", tagDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 删除标签
     * @param ids
     * @return
     */
    @Override
    public Response delTags(Set<String> ids) {
        // 如果标签已经绑定了博客，那么不删除该标签
        List<String> delFailedTagNames = new ArrayList<>();
        for (String id : ids) {
            List<String> blogIdsByTagId = baseMapper.selectBlogIdsByTagId(id);
            if (!CollectionUtils.isEmpty(blogIdsByTagId)) {
                Tag tag = baseMapper.selectById(id);
                LOGGER.warn("delete tag failed, the tag:{} has been bound", tag);
                delFailedTagNames.add(tag.getTagName());
                continue;
            }
            if (baseMapper.deleteById(id) < 1) {
                LOGGER.error("delTags failed by unknown error, roleId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        if (CollectionUtils.isEmpty(delFailedTagNames)) {
            return Response.ok();
        }
        return Response.error().message(delFailedTagNames.toString() + "已绑定用户, 未删除成功");
    }

    private Tag toTag(TagDto tagDto) {
        Tag tag = new Tag();
        BeanUtil.copyProperties(tagDto, tag);
        tag.setId(tagDto.getId());
        return tag;
    }

    private List<TagDto> toDto(List<Tag> tags) {
        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto tagDto = new TagDto();
            BeanUtil.copyProperties(tag, tagDto);
            tagDto.setId(tag.getId());
            tagDto.setCreateTime(tag.getCreateTime());
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }


}
