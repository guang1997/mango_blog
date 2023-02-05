package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Comment;
import com.myblog.service.admin.entity.dto.CommentDto;
import com.myblog.service.admin.mapper.CommentMapper;
import com.myblog.service.admin.service.CommentService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-11
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    /**
     * 分页查询评论
     *
     * @param commentDto
     * @return
     */
    @Override
    public Map<String, Object> getCommentByPage(CommentDto commentDto) throws Exception {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(commentDto.getNickname())) {
            queryWrapper.like(DbConstants.Admin.NICKNAME, commentDto.getNickname());
        }
        if (Objects.nonNull(commentDto.getSource())) {
            queryWrapper.eq(DbConstants.Comment.SOURCE, commentDto.getSource());
        }
        if (Objects.nonNull(commentDto.getType())) {
            queryWrapper.eq(DbConstants.Comment.TYPE, commentDto.getType());
        }
        if (StringUtils.isNotBlank(commentDto.getContent())) {
            queryWrapper.like(DbConstants.Comment.CONTENT, commentDto.getContent());
        }
        if (!CollectionUtils.isEmpty(commentDto.getCreateTimes()) && Objects.equals(2, commentDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(commentDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(commentDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        queryWrapper.eq(DbConstants.Comment.STATUS, 1);
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        Page<Comment> commentPage = new Page<>(commentDto.getPage(), commentDto.getSize());
        baseMapper.selectPage(commentPage, queryWrapper);

        Map<String, Object> resultMap = new HashMap<>();
        List<CommentDto> commentDtos = this.toDtoList(commentPage.getRecords(), CommentDto.class);
        resultMap.put(Constants.ReplyField.DATA, commentDtos);
        resultMap.put(Constants.ReplyField.TOTAL, commentPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, commentDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, commentDto.getSize());
        return resultMap;
    }

    /**
     * 删除评论
     * @param ids
     * @return
     */
    @Override
    public Boolean delComment(Set<String> ids) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(DbConstants.Base.ID, ids);
        List<Comment> comments = baseMapper.selectList(queryWrapper);
        // 查询所有需要删除的评论
        Set<Comment> delComments = new HashSet<>();
        getComments(comments, delComments);

        List<String> delIds = delComments.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        QueryWrapper<Comment> delWrapper = new QueryWrapper<>();
        delWrapper.in(DbConstants.Base.ID, delIds);
        if (baseMapper.delete(delWrapper) < 1) {
            log.error("delComment failed by unknown error, commentIds:{}", delIds);
            return false;
        }
        return true;
    }

    private void getComments(List<Comment> comments, Set<Comment> delComments) {
        if (CollectionUtils.isEmpty(comments)) {
            return;
        }
        for (Comment comment : comments) {
            delComments.add(comment);
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DbConstants.Comment.PARENT_ID, comment.getId());
            List<Comment> childrenComments = baseMapper.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(childrenComments)) {
                getComments(childrenComments, delComments);
            }
        }
    }

    /**
     * 获取特定类型的评论数目
     * @param status
     * @return
     */
    @Override
    public int getCommentCount(int status) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Comment.STATUS, status);
        Integer count = baseMapper.selectCount(queryWrapper);
        return Objects.isNull(count) ? 0 : count;
    }
}
