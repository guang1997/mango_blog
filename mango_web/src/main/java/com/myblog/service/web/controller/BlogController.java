package com.myblog.service.web.controller;


import com.myblog.service.base.common.BehaviorEnum;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Response getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getBlogByPage(blogDto);
    }

    @LogByMethod(value = "/web/blog/getBlogById", behavior = BehaviorEnum.BLOG_DETAIL)
    @ApiOperation(value = "根据博客id查询博客信息", notes = "根据博客id查询博客信息", response = Response.class)
    @PostMapping("/getBlogById")
    public Response getBlogById(@RequestBody BlogDto blogDto, HttpServletRequest request) throws Exception {
        Response response = blogService.getBlogById(blogDto, request);
        CommentDto param = new CommentDto();
        param.setPage(blogDto.getPage());
        param.setSize(blogDto.getSize());
        param.setBlogId(blogDto.getId());
        param.setQueryLike(true);
        param.setUserId(blogDto.getUserId());
        Response commentResponse = commentService.getCommentByPage(param);
        Map<String, Object> commentData = commentResponse.getData();
        response.data(Constants.ReplyField.COMMENT, commentData.get(Constants.ReplyField.DATA));
        response.data(Constants.ReplyField.COMMENT_COUNT, commentData.get(Constants.ReplyField.TOTAL));
        return response;
    }

    @LogByMethod(value = "/web/blog/getBlogBySortId")
    @ApiOperation(value = "根据分类id查询博客信息", notes = "根据分类id查询博客信息", response = Response.class)
    @PostMapping("/getBlogBySortId")
    public Response getBlogBySortId(@RequestBody BlogDto blogDto) throws Exception {
        if (StringUtils.isBlank(blogDto.getBlogSortId())) {
            log.warn("getBlogBySortId return empty, blogSortId is null, blogDto:{}", blogDto);
            return Response.ok();
        }
        return blogService.getBlogBySortId(blogDto);
    }

    @LogByMethod(value = "/web/blog/getBlogByTagId")
    @ApiOperation(value = "根据标签id查询博客信息", notes = "根据分类id查询博客信息", response = Response.class)
    @PostMapping("/getBlogByTagId")
    public Response getBlogByTagId(@RequestBody BlogDto blogDto) throws Exception {
        if (StringUtils.isBlank(blogDto.getTagId())) {
            log.warn("getBlogByTagId return empty, tagId is null, blogDto:{}", blogDto);
            return Response.ok();
        }
        return blogService.getBlogByTagId(blogDto);
    }

    @LogByMethod(value = "/web/blog/getPrevNextBlog")
    @ApiOperation(value = "查询博客上一篇博客和下一篇博客", notes = "查询博客上一篇博客和下一篇博客", response = Response.class)
    @PostMapping("/getPrevNextBlog")
    public Response getPrevNextBlog(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getPrevNextBlog(blogDto);
    }

    @LogByMethod(value = "/web/blog/fuzzy")
    @ApiOperation(value = "模糊查询", notes = "模糊查询", response = Response.class)
    @PostMapping("/getPrevNextBlog")
    public Response fuzzy(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.fuzzy(blogDto);
    }
}

