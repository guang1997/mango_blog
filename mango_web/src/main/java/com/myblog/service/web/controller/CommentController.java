package com.myblog.service.web.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.CommentDto;
import com.myblog.service.web.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @LogByMethod("/web/comment/getCommentByPage")
    @ApiOperation(value = "分页查询评论信息", notes = "分页查询评论信息", response = Response.class)
    @PostMapping("/getCommentByPage")
    public Response getCommentByPage(@RequestBody CommentDto commentDto) throws Exception {
        return commentService.getCommentByPage(commentDto);
    }

    @LogByMethod("/web/comment/likeBlog")
    @ApiOperation(value = "给博客点赞", notes = "给博客点赞", response = Response.class)
    @PostMapping("/likeBlog")
    public Response likeBlog(@RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        return commentService.likeBlog(commentDto, request);
    }

    @LogByMethod("/web/comment/saveComment")
    @ApiOperation(value = "保存评论", notes = "保存评论", response = Response.class)
    @PostMapping("/saveComment")
    public Response saveComment(@RequestBody CommentDto commentDto, HttpServletRequest request) throws Exception {
        return commentService.saveComment(commentDto, request);
    }
}

