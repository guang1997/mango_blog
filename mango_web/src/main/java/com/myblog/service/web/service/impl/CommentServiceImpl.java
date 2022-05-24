package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.CommentSourceEnum;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.web.entity.Comment;
import com.myblog.service.web.entity.dto.CommentDto;
import com.myblog.service.web.mapper.BlogMapper;
import com.myblog.service.web.mapper.CommentMapper;
import com.myblog.service.web.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(commentPage, queryWrapper);

        response.data(Constants.ReplyField.DATA, this.toDtoList(commentPage.getRecords(), CommentDto.class));
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
        comment.setIp(IpUtils.getIpAddr(request));
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
        commentUpdateWrapper.eq(DbConstants.Comment.USER_ID, comment.getUserId());
        // TODO (IP+操作系统类型+浏览器类型+浏览器版本号) 生成IP唯一标识
//        commentUpdateWrapper.and(wrapper -> wrapper.eq(DbConstants.Comment.IP, comment.getIp()).or().eq(DbConstants.Comment.USER_ID, comment.getUserId()));

        if (baseMapper.update(comment, commentUpdateWrapper) < 1) {
            if (baseMapper.insert(comment) < 1) {
                LOGGER.error("likeBlog failed by unknowen error, comment:{}", comment);
                return Response.error().message(failedMsg);
            }
        }
        response.setMessage(successMsg);
        return response;
    }
}
