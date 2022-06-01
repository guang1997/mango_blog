package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.*;
import com.myblog.service.base.util.BaseUtil;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.MD5Utils;
import com.myblog.service.web.entity.Comment;
import com.myblog.service.web.entity.dto.CommentDto;
import com.myblog.service.web.mapper.BlogMapper;
import com.myblog.service.web.mapper.CommentMapper;
import com.myblog.service.web.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.web.util.CommentTreeUtil;
import com.myblog.service.web.util.UniqueKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private static Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Response getCommentByPage(CommentDto commentDto) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 5;
        if (Objects.nonNull(commentDto.getPage())) page = commentDto.getPage();
        if (Objects.nonNull(commentDto.getSize())) size = commentDto.getSize();

        Page<Comment> commentPage = new Page<>(page, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(commentDto.getBlogId())) {
            queryWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
        }
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        queryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.MESSAGE);
        queryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommonStatus.ENABLED);
        baseMapper.selectPage(commentPage, queryWrapper);
        List<CommentDto> commentDtos = this.toDtoList(commentPage.getRecords(), CommentDto.class);
        // 查询子评论
        if (Objects.nonNull(commentDto.getQueryChildren()) && commentDto.getQueryChildren()) {
            QueryWrapper<Comment> childrenQueryWrapper = new QueryWrapper<>();
            childrenQueryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
            childrenQueryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.MESSAGE);
            childrenQueryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommonStatus.ENABLED);
            if (StringUtils.isNotBlank(commentDto.getBlogId())) {
                // 博客详情页面
                childrenQueryWrapper.eq(DbConstants.Comment.SOURCE, CommentSourceEnum.BLOG_INFO_MESSAGE);
                childrenQueryWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
            } else {
                // 留言板
            }
            commentDtos.addAll(this.toDtoList(baseMapper.selectList(childrenQueryWrapper), CommentDto.class));
            commentDtos = commentDtos.stream().filter(BaseUtil.distinctByKey(CommentDto::getId)).collect(Collectors.toList());
            commentDtos = CommentTreeUtil.toCommentTree(commentDtos, "0");
        }

        response.data(Constants.ReplyField.DATA, commentDtos);
        response.data(Constants.ReplyField.TOTAL, commentPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 给博客点赞
     *
     * @param commentDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response likeBlog(CommentDto commentDto, HttpServletRequest request) throws Exception {
        int likeCount = 0;
        String successMsg = "点赞成功";
        String failedMsg = "点赞失败";
        Response response = Response.ok();
        // 注意：userId可能是浏览器生成的指纹，也可能是用户登陆后生成的用户id
        Comment comment = this.toDb(commentDto, Comment.class);
        comment.setStatus(Constants.CommentStatus.REVIEWED);
        comment.setBlogId(commentDto.getBlogId());
        String ipAddr = IpUtils.getIpAddr(request);
        comment.setIp(ipAddr);
        String uniqueKey = UniqueKeyUtil.getUniqueKey(request, commentDto.getScreenInformation());
        LOGGER.debug("likeBlog getUniqueKey:{} success", uniqueKey);
        comment.setUniqueKey(MD5Utils.string2MD5(uniqueKey));

        comment.setType(Constants.CommentType.LIKES);
        comment.setSource(CommentSourceEnum.BLOG_INFO_LIKES.getSource());
        comment.setParentId("0");
        // 已经点过赞，要取消点赞
        if (commentDto.getIsLiked()) {
            likeCount = commentDto.getLikeCount() - 1;
            comment.setContent("false");
            successMsg = "取消的是赞，受伤的是心 ಥ﹏ಥ";
            failedMsg = "点赞取消失败";
        } else {
            comment.setContent("true");
            likeCount = commentDto.getLikeCount() + 1;
        }

        if (blogMapper.changeLike(commentDto.getBlogId(), likeCount) < 1) {
            LOGGER.error("likeBlog failed by change blog likeCount, commentDto:{}", commentDto);
            return Response.error().message(failedMsg);
        }
        UpdateWrapper<Comment> commentUpdateWrapper = new UpdateWrapper<>();
        commentUpdateWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
        commentUpdateWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
        // TODO 暂时先只通过浏览器指纹或者用户id以及ip和设备型号生成的数据来设置点赞,尽量保证唯一性
        commentUpdateWrapper.and(wrapper -> wrapper
                .and(wrapper2 -> wrapper2.eq(DbConstants.Comment.IP, comment.getIp()).eq(DbConstants.Comment.UNIQUE_KEY, comment.getUniqueKey()))
                .or().eq(DbConstants.Comment.USER_ID, comment.getUserId()));

        if (baseMapper.update(comment, commentUpdateWrapper) < 1) {
            if (baseMapper.insert(comment) < 1) {
                LOGGER.error("likeBlog failed by unknowen error, comment:{}", comment);
                return Response.error().message(failedMsg);
            }
        }
        response.setMessage(successMsg);
        return response;
    }

    /**
     * 保存评论
     *
     * @param commentDto
     * @return
     */
    @Override
    public Response saveComment(CommentDto commentDto, HttpServletRequest request) throws Exception {
        Comment comment = this.toDb(commentDto, Comment.class);
        comment.setIp(IpUtils.getIpAddr(request));
        if (baseMapper.insert(comment) < 1) {
            LOGGER.error("likeBlog failed by unknowen error, comment:{}", comment);
            return Response.setResult(ResultCodeEnum.COMMENT_FAILED);
        }
        return Response.ok();
    }
}
