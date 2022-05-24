package com.myblog.service.web.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.BlogDto;
import com.myblog.service.web.service.BlogService;
import com.myblog.service.web.service.impl.SortServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-17
 */
@CrossOrigin
@RestController
@RequestMapping("/web/blog")
public class BlogController {

    private static Logger LOGGER = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @LogByMethod("/web/blog/getBlogByPage")
    @ApiOperation(value = "分页查询博客信息", notes = "分页查询博客信息", response = Response.class)
    @PostMapping("/getBlogByPage")
    public Response getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getBlogByPage(blogDto);
    }

    @LogByMethod("/web/blog/getBlogById")
    @ApiOperation(value = "根据博客id查询博客信息", notes = "根据博客id查询博客信息", response = Response.class)
    @PostMapping("/getBlogById")
    public Response getBlogById(@RequestBody BlogDto blogDto, HttpServletRequest request) throws Exception {
        return blogService.getBlogById(blogDto, request);
    }

    @LogByMethod("/web/blog/getBlogBySortId")
    @ApiOperation(value = "根据分类id查询博客信息", notes = "根据分类id查询博客信息", response = Response.class)
    @PostMapping("/getBlogBySortId")
    public Response getBlogBySortId(@RequestBody BlogDto blogDto) throws Exception {
        if (StringUtils.isBlank(blogDto.getBlogSortId())) {
            LOGGER.warn("getBlogBySortId return empty, blogSortId is null, blogDto:{}", blogDto);
            return Response.ok();
        }
        return blogService.getBlogBySortId(blogDto);
    }

    @LogByMethod("/web/blog/getBlogByTagId")
    @ApiOperation(value = "根据标签id查询博客信息", notes = "根据分类id查询博客信息", response = Response.class)
    @PostMapping("/getBlogByTagId")
    public Response getBlogByTagId(@RequestBody BlogDto blogDto) throws Exception {
        if (StringUtils.isBlank(blogDto.getTagId())) {
            LOGGER.warn("getBlogByTagId return empty, tagId is null, blogDto:{}", blogDto);
            return Response.ok();
        }
        return blogService.getBlogByTagId(blogDto);
    }

    @LogByMethod("/web/blog/getPrevNextBlog")
    @ApiOperation(value = "查询博客上一篇博客和下一篇博客", notes = "查询博客上一篇博客和下一篇博客", response = Response.class)
    @PostMapping("/getPrevNextBlog")
    public Response getPrevNextBlog(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getPrevNextBlog(blogDto);
    }
}

