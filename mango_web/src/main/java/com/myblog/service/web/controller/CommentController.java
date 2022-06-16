package com.myblog.service.web.controller;


import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.CommentDto;
import com.myblog.service.web.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@CrossOrigin
@RestController
@RequestMapping("/web/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @LogByMethod(value = "/web/comment/getCommentByPage")
    @ApiOperation(value = "分页查询评论信息", notes = "分页查询评论信息", response = Response.class)
    @PostMapping("/getCommentByPage")
    public Response getCommentByPage(@RequestBody CommentDto commentDto) throws Exception {
        return commentService.getCommentByPage(commentDto);
    }

    @LogByMethod(value = "/web/comment/getMessageBoardCommentByPage", behavior = BehaviorEnum.MESSAGE_BOARD)
    @ApiOperation(value = "留言板页面分页查询评论信息", notes = "留言板页面分页查询评论信息", response = Response.class)
    @PostMapping("/getMessageBoardCommentByPage")
    public Response getMessageBoardCommentByPage(@RequestBody CommentDto commentDto) throws Exception {
        return commentService.getCommentByPage(commentDto);
    }

    @LogByMethod(value = "/web/comment/likeBlog", behavior = BehaviorEnum.LIKE)
    @ApiOperation(value = "给博客点赞", notes = "给博客点赞", response = Response.class)
    @PostMapping("/likeBlog")
    public Response likeBlog(@RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        return commentService.likeBlog(commentDto, request);
    }

    @LogByMethod(value = "/web/comment/saveComment", behavior = BehaviorEnum.PUBLISH_COMMENT)
    @ApiOperation(value = "保存评论", notes = "保存评论", response = Response.class)
    @PostMapping("/saveComment")
    public Response saveComment(@RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        return commentService.saveComment(commentDto, request);
    }

    @LogByMethod(value = "/web/comment/likeComment", behavior = BehaviorEnum.LIKE)
    @ApiOperation(value = "给评论点赞", notes = "给评论点赞", response = Response.class)
    @PostMapping("/likeComment")
    public Response likeComment(@RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        return commentService.likeComment(commentDto, request);
    }
}

