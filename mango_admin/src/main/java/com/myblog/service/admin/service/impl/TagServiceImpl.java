package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Tag;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.mapper.TagMapper;
import com.myblog.service.admin.service.TagService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    /**
     * 分页查询标签信息
     * @param tagDto
     * @return
     * @throws ParseException
     */
    @Override
    public Response getTagByPage(TagDto tagDto) throws Exception {
        Response response = Response.ok();
        if (Objects.nonNull(tagDto.getQueryAll()) && tagDto.getQueryAll()) {
            List<TagDto> tagDtos = this.toDtoList(baseMapper.selectList(null), TagDto.class);
            response.data(Constants.ReplyField.DATA, tagDtos);
            return response;
        }
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
        Page<Tag> tagPage = new Page<>(page, size);

        baseMapper.selectPage(tagPage, queryWrapper);

        List<TagDto> tagDtos = this.toDtoList(tagPage.getRecords(), TagDto.class);
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
    public Response addTag(TagDto tagDto) throws Exception{
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Tag.TAG_NAME, tagDto.getTagName());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            log.error("addTag failed, tagName is already exist, tag:{}", tagDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Tag tag = this.toDb(tagDto, Tag.class);
        if (baseMapper.insert(tag) < 1) {
            log.error("addTag failed by unknown error, tag:{}", tag);
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
    public Response editTag(TagDto tagDto) throws Exception{
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Tag.TAG_NAME, tagDto.getTagName());
        List<Tag> tags = baseMapper.selectList(queryWrapper);
        if (tags.size() > 0) {
            for (Tag tag : tags) {
                if (!Objects.equals(tag.getId(), tagDto.getId())) {
                    return Response.error().message("更新失败, 已存在相同名称的标签");
                }
            }
        }
        Tag tag = this.toDb(tagDto, Tag.class);
        if (baseMapper.updateById(tag) < 1) {
            log.error("editTag failed by unknown error, tag:{}", tag);
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
    public Response delTags(Set<String> ids) throws Exception{
        // 如果标签已经绑定了博客，那么不删除该标签
        List<String> delFailedTagNames = new ArrayList<>();
        for (String id : ids) {
            List<String> blogIdsByTagId = baseMapper.selectBlogIdsByTagId(id);
            if (!CollectionUtils.isEmpty(blogIdsByTagId)) {
                Tag tag = baseMapper.selectById(id);
                log.warn("delete tag failed, the tag:{} has been bound", tag);
                delFailedTagNames.add(tag.getTagName());
                continue;
            }
            if (baseMapper.deleteById(id) < 1) {
                log.error("delTags failed by unknown error, tagId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        if (CollectionUtils.isEmpty(delFailedTagNames)) {
            return Response.ok();
        }
        return Response.error().message(delFailedTagNames.toString() + "已绑定博客, 未删除成功");
    }
}
