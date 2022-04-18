package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.dto.AdminDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private BlogService blogService;

    @LogByMethod("/admin/blog/getBlogByPage")
    @ApiOperation(value = "分页查询博客信息", notes = "分页查询博客信息", response = Response.class)
    @PostMapping("/getBlogByPage")
    public Response getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getBlogByPage(blogDto);
    }
}

