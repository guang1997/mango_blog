package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.*;
import com.myblog.service.base.exception.BusinessException;
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
import org.apache.commons.lang3.BooleanUtils;
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
    public Map<String, Object> getCommentByPage(CommentDto commentDto) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        Page<Comment> commentPage = new Page<>(commentDto.getPage(), commentDto.getSize());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(commentDto.getBlogId())) {
            queryWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
        }
        if (StringUtils.isNotBlank(commentDto.getSource())) {
            queryWrapper.eq(DbConstants.Comment.SOURCE, commentDto.getSource());
        }
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        queryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.MESSAGE);
        queryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommonStatus.ENABLED);
        baseMapper.selectPage(commentPage, queryWrapper);
        List<CommentDto> commentDtos = this.toDtoList(commentPage.getRecords(), CommentDto.class);
        // 查询用户是否已经给评论点赞
        if (BooleanUtils.isTrue(commentDto.getQueryLike()) && Objects.nonNull(commentDto.getUserId())) {
            QueryWrapper<Comment> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.eq(DbConstants.Comment.STATUS, Constants.CommonStatus.ENABLED);
            likeQueryWrapper.eq(DbConstants.Comment.USER_ID, commentDto.getUserId());
            if (StringUtils.isNotBlank(commentDto.getBlogId())) {
                likeQueryWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
            }
            likeQueryWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
            likeQueryWrapper.in(DbConstants.Comment.SOURCE, CommentSourceEnum.getLikeSourceList());
            commentDtos.addAll(this.toDtoList(baseMapper.selectList(likeQueryWrapper), CommentDto.class));
        }
        commentDtos = CommentTreeUtil.toCommentTree(commentDtos, "0");
        resultMap.put(Constants.ReplyField.DATA, commentDtos);
        resultMap.put(Constants.ReplyField.TOTAL, commentPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, commentDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, commentDto.getSize());
        return resultMap;
    }

    /**
     * 给博客点赞
     *
     * @param commentDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String likeBlog(CommentDto commentDto, HttpServletRequest request) throws Exception {
        int likeCount = 0;
        String successMsg = "点赞成功";
        String failedMsg = "点赞失败";
        Comment comment = this.toDb(commentDto, Comment.class);
        comment.setStatus(Constants.CommentStatus.REVIEWED);
        comment.setBlogId(commentDto.getBlogId());
        String ipAddr = IpUtils.getIpAddr(request);
        comment.setIp(ipAddr);
        String uniqueKey = UniqueKeyUtil.getUniqueKey(request, commentDto.getScreenInformation());
        LOGGER.debug("likeBlog getUniqueKey:{} success", uniqueKey);
        comment.setUniqueKey(MD5Utils.string2MD5(uniqueKey));
        comment.setType(Constants.CommentType.LIKES);
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
        // 如果userId不为空，说明登录了账号，直接根据账号来更新点赞状态
        if (StringUtils.isNotBlank(commentDto.getUserId())) {
            UpdateWrapper<Comment> userIdUpdateWrapper = new UpdateWrapper<>();
            userIdUpdateWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
            userIdUpdateWrapper.eq(DbConstants.Comment.SOURCE, commentDto.getSource());
            userIdUpdateWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
            userIdUpdateWrapper.eq(DbConstants.Comment.USER_ID, commentDto.getUserId());
            if (baseMapper.update(comment, userIdUpdateWrapper) < 1) {
                if (baseMapper.insert(comment) < 1) {
                    LOGGER.error("likeBlog failed by unknowen error, comment:{}", comment);
                    throw new BusinessException(failedMsg);
                }
            }
        } else {
            // 如果userId为空，使用浏览器指纹和ip以及uniqueKey来更新点赞状态
            UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
            updateWrapper.eq(DbConstants.Comment.SOURCE, commentDto.getSource());
            updateWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
            updateWrapper.and(wrapper -> wrapper
                    .and(wrapper2 -> wrapper2.eq(DbConstants.Comment.IP, comment.getIp()).eq(DbConstants.Comment.UNIQUE_KEY, comment.getUniqueKey()))
                    .or().eq(DbConstants.Comment.BROWSER_FINGER, comment.getBrowserFinger()));

            updateWrapper.isNull(DbConstants.Comment.USER_ID);
            if (baseMapper.update(comment, updateWrapper) < 1) {
                if (baseMapper.insert(comment) < 1) {
                    LOGGER.error("likeBlog failed by unknowen error, comment:{}", comment);
                    throw new BusinessException(failedMsg);
                }
            }
        }
        if (blogMapper.changeLike(commentDto.getBlogId(), likeCount) < 1) {
            LOGGER.error("likeBlog failed by change blog likeCount, commentDto:{}", commentDto);
            throw new BusinessException(failedMsg);
        }
        return successMsg;
    }

    /**
     * 保存评论
     *
     * @param commentDto
     * @return
     */
    @Override
    public Boolean saveComment(CommentDto commentDto, HttpServletRequest request) throws Exception {
        Comment comment = this.toDb(commentDto, Comment.class);
        comment.setIp(IpUtils.getIpAddr(request));
        if (baseMapper.insert(comment) < 1) {
            LOGGER.error("likeBlog failed by unknowen error, comment:{}", comment);
            return false;
        }
        return true;
    }

    /**
     * 给评论点赞
     * @param commentDto
     * @param request
     * @return
     */
    @Override
    public String likeComment(CommentDto commentDto, HttpServletRequest request) throws Exception{
        String successMsg = "点赞成功";
        String failedMsg = "点赞失败";
        Comment comment = this.toDb(commentDto, Comment.class);
        comment.setStatus(Constants.CommentStatus.REVIEWED);
        comment.setBlogId(commentDto.getBlogId());
        String ipAddr = IpUtils.getIpAddr(request);
        comment.setIp(ipAddr);
        comment.setType(Constants.CommentType.LIKES);
        Comment parentComment = baseMapper.selectById(commentDto.getParentId());
        // 已经点过赞，要取消点赞
        if (commentDto.getIsLiked()) {
            comment.setContent("false");
            successMsg = "取消的是赞，受伤的是心 ಥ﹏ಥ";
            failedMsg = "点赞取消失败";
            parentComment.setLikeCount(parentComment.getLikeCount() <= 0 ? 0 : parentComment.getLikeCount() - 1);
        } else {
            comment.setContent("true");
            parentComment.setLikeCount(parentComment.getLikeCount() + 1);
        }
        UpdateWrapper<Comment> userIdUpdateWrapper = new UpdateWrapper<>();
        userIdUpdateWrapper.eq(DbConstants.Comment.TYPE, Constants.CommentType.LIKES);
        userIdUpdateWrapper.eq(DbConstants.Comment.SOURCE, commentDto.getSource());
        if (StringUtils.isNotBlank(commentDto.getBlogId())) {
            userIdUpdateWrapper.eq(DbConstants.Comment.BLOG_ID, commentDto.getBlogId());
        }
        userIdUpdateWrapper.eq(DbConstants.Comment.USER_ID, commentDto.getUserId());
        userIdUpdateWrapper.eq(DbConstants.Comment.PARENT_ID, commentDto.getParentId());
        if (baseMapper.update(comment, userIdUpdateWrapper) < 1) {
            if (baseMapper.insert(comment) < 1) {
                LOGGER.error("likeComment failed by unknowen error, comment:{}", comment);
                throw new BusinessException(failedMsg);
            }
        }
        baseMapper.updateById(parentComment);
        return successMsg;
    }
}
