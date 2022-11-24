package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.CommentDto;
import com.myblog.service.admin.service.CommentService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-11
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @LogByMethod("/admin/comment/getCommentByPage")
    @ApiOperation(value = "分页查询评论", notes = "分页查询评论", response = Response.class)
    @PostMapping("/getCommentByPage")
    public Response getCommentByPage(@RequestBody CommentDto commentDto) throws Exception {
        return commentService.getCommentByPage(commentDto);
    }

    @LogByMethod("/admin/comment/delComment")
    @ApiOperation(value = "删除评论", notes = "删除评论", response = Response.class)
    @DeleteMapping("/delComment")
    public Response delComment(@RequestBody Set<String> ids) throws Exception {
        return commentService.delComment(ids);
    }
}

