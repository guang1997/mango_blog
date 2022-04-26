package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.RoleDto;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger LOGGER = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @LogByMethod("/admin/blog/getBlogByPage")
    @ApiOperation(value = "分页查询博客信息", notes = "分页查询博客信息", response = Response.class)
    @PostMapping("/getBlogByPage")
    public Response getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return blogService.getBlogByPage(blogDto);
    }

    @LogByMethod("/admin/role/addBlog")
    @ApiOperation(value = "新增博客", notes = "新增博客", response = Response.class)
    @PostMapping("/addBlog")
    public Response addBlog(@RequestBody BlogDto blogDto) throws Exception {
        if (StringUtils.isBlank(blogDto.getTitle())
                || StringUtils.isBlank(blogDto.getContent())
                || StringUtils.isBlank(blogDto.getFileId())) {
            LOGGER.error("addBlog failed, title or content or fileId cannot be null, blog:{}", blogDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return blogService.addBlog(blogDto);
    }
}

