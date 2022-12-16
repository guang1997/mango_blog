package com.myblog.service.admin.controller;


import com.myblog.service.admin.config.QiNiuYunOssProperties;
import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private OssService ossService;

    @Autowired
    private QiNiuYunOssProperties qiNiuYunOssProperties;

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
        Response response = blogService.delBlog(ids);
        if (response.getSuccess() && qiNiuYunOssProperties.getDeletePicture()) {
            try {
                // 博客删除成功后删除七牛云图片 TODO 目前只删除标题图片, 后续删除内容中的图片
                List<String> urlList = (List<String>) response.getData().get(Constants.ReplyField.DELETED_FILE_LIST);
                for (String url : urlList) {
                    ossService.delete(url);
                }
            } catch (Exception e) {
                // 图片删除失败不影响博客删除
                log.error("delBlog success but delete blog fileId failed, blogIds:{}", ids);
            }
        }
        return response;
    }
}

