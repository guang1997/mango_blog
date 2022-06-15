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
import com.myblog.service.base.util.ThreadSafeDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-11
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private static Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    /**
     * 分页查询评论
     *
     * @param commentDto
     * @return
     */
    @Override
    public Response getCommentByPage(CommentDto commentDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(commentDto.getPage())) page = commentDto.getPage();
        if (Objects.nonNull(commentDto.getSize())) size = commentDto.getSize();
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
        Page<Comment> commentPage = new Page<>(page, size);
        baseMapper.selectPage(commentPage, queryWrapper);

        List<CommentDto> commentDtos = this.toDtoList(commentPage.getRecords(), CommentDto.class);
        response.data(Constants.ReplyField.DATA, commentDtos);
        response.data(Constants.ReplyField.TOTAL, commentPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 删除评论
     * @param ids
     * @return
     */
    @Override
    public Response delComment(Set<String> ids) {
        for (String id : ids) {
            if (baseMapper.deleteById(id) < 1) {
                LOGGER.error("delComment failed by unknown error, commentId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        return Response.ok();
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
