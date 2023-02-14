package com.myblog.service.web.controller;


import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.Constants.ReplyField;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.base.handler.es.entity.BlogEsDto;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.BlogDto;
import com.myblog.service.web.entity.dto.CommentDto;
import com.myblog.service.web.service.BlogService;
import com.myblog.service.web.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-17
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/web/blog")
public class BlogController {


    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @LogByMethod(value = "/web/blog/getBlogByPage")
    @ApiOperation(value = "分页查询博客信息", notes = "分页查询博客信息", response = Response.class)
    @PostMapping("/getBlogByPage")
    public ResultModel<Map<String, Object>> getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return ResultModel.ok(blogService.getBlogByPage(blogDto));
    }

    @LogByMethod(value = "/web/blog/getBlogById", behavior = BehaviorEnum.BLOG_DETAIL)
    @ApiOperation(value = "根据博客id查询博客信息", notes = "根据博客id查询博客信息", response = Response.class)
    @PostMapping("/getBlogById")
    public ResultModel<Map<String, Object>> getBlogById(@RequestBody BlogDto blogDto, HttpServletRequest request) throws Exception {
        BlogDto responseBlogDto = blogService.getBlogById(blogDto, request);
        CommentDto param = new CommentDto();
        param.setPage(blogDto.getPage());
        param.setSize(blogDto.getSize());
        param.setBlogId(blogDto.getId());
        param.setQueryLike(true);
        param.setUserId(blogDto.getUserId());
        Map<String, Object> commentResponse = commentService.getCommentByPage(param);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(ReplyField.DATA, responseBlogDto);
        resultMap.put(Constants.ReplyField.COMMENT, commentResponse.get(Constants.ReplyField.DATA));
        resultMap.put(Constants.ReplyField.COMMENT_COUNT, commentResponse.get(Constants.ReplyField.TOTAL));
        return ResultModel.ok(resultMap);
    }

    @LogByMethod(value = "/web/blog/getBlogBySortId")
    @ApiOperation(value = "根据分类id查询博客信息", notes = "根据分类id查询博客信息", response = Response.class)
    @PostMapping("/getBlogBySortId")
    public ResultModel<List<BlogDto>> getBlogBySortId(@RequestBody @Validated(value = BlogDto.BlogValidGroup.GetBlogBySortId.class) BlogDto blogDto) throws Exception {
        return ResultModel.ok(blogService.getBlogBySortId(blogDto));
    }

    @LogByMethod(value = "/web/blog/getBlogByTagId")
    @ApiOperation(value = "根据标签id查询博客信息", notes = "根据分类id查询博客信息", response = Response.class)
    @PostMapping("/getBlogByTagId")
    public ResultModel<List<BlogDto>> getBlogByTagId(@RequestBody @Validated(value = BlogDto.BlogValidGroup.GetBlogByTagId.class) BlogDto blogDto) throws Exception {
        return ResultModel.ok(blogService.getBlogByTagId(blogDto));
    }

    @LogByMethod(value = "/web/blog/getPrevNextBlog")
    @ApiOperation(value = "查询博客上一篇博客和下一篇博客", notes = "查询博客上一篇博客和下一篇博客", response = Response.class)
    @PostMapping("/getPrevNextBlog")
    public ResultModel<Map<String, Object>> getPrevNextBlog(@RequestBody BlogDto blogDto) throws Exception {
        return ResultModel.ok(blogService.getPrevNextBlog(blogDto));
    }

    @LogByMethod(value = "/web/blog/getBlogByKeyword")
    @ApiOperation(value = "模糊查询", notes = "模糊查询", response = Response.class)
    @PostMapping("/getBlogByKeyword")
    public ResultModel<Map<String, Object>> getBlogByKeyword(@RequestBody BlogDto blogDto) throws Exception {
        return ResultModel.ok(blogService.getBlogByKeyword(blogDto));
    }
}

