package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/blog")
public class BlogController {

    private static Logger LOGGER = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @LogByMethod("/admin/blog/getBlogByPage")
    @ApiOperation(value = "分页查询博客信息", notes = "分页查询博客信息", response = Response.class)
    @PostMapping("/getBlogByPage")
    public Response getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getBlogByPage(blogDto);
    }

    @LogByMethod(value = "/admin/role/addBlog", validate = true)
    @ApiOperation(value = "新增博客", notes = "新增博客", response = Response.class)
    @PostMapping("/addBlog")
    public Response addBlog(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.addBlog(blogDto);
    }

    @LogByMethod(value = "/admin/blog/editBlog", validate = true)
    @ApiOperation(value = "修改博客", notes = "修改博客", response = Response.class)
    @PutMapping("/editBlog")
    public Response editBlog(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.editBlog(blogDto);
    }

    @LogByMethod("/admin/blog/delBlog")
    @ApiOperation(value = "删除博客", notes = "删除博客", response = Response.class)
    @DeleteMapping("/delBlog")
    public Response delBlog(@RequestBody Set<String> ids) throws Exception {
        return blogService.delBlog(ids);
    }
}

