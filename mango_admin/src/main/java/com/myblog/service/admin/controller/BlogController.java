package com.myblog.service.admin.controller;


import com.myblog.service.admin.config.QiNiuYunOssProperties;
import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResultModel<Map<String, Object>> getBlogByPage(@RequestBody BlogDto blogDto) throws Exception {
        return ResultModel.ok(blogService.getBlogByPage(blogDto));
    }

    @LogByMethod(value = "/admin/role/addBlog", validate = true)
    @ApiOperation(value = "新增博客", notes = "新增博客", response = Response.class)
    @PostMapping("/addBlog")
    public ResultModel<Object> addBlog(@RequestBody @Validated BlogDto blogDto) throws Exception {
        if (blogService.addBlog(blogDto)) {
            return ResultModel.ok();
        }
        return ResultModel.error();
    }

    @LogByMethod(value = "/admin/blog/editBlog", validate = true)
    @ApiOperation(value = "修改博客", notes = "修改博客", response = Response.class)
    @PutMapping("/editBlog")
    public ResultModel<Object> editBlog(@RequestBody @Validated BlogDto blogDto) throws Exception {
        if (blogService.editBlog(blogDto)) {
            return ResultModel.ok();
        }
        return ResultModel.error();
    }

    @LogByMethod("/admin/blog/delBlog")
    @ApiOperation(value = "删除博客", notes = "删除博客", response = Response.class)
    @DeleteMapping("/delBlog")
    public ResultModel<Object> delBlog(@RequestBody Set<String> ids) throws Exception {
        List<String> fileIdList = blogService.delBlog(ids);
        if (!CollectionUtils.isEmpty(fileIdList) && qiNiuYunOssProperties.getDeletePicture()) {
            try {
                // 博客删除成功后删除七牛云图片 TODO 目前只删除标题图片, 后续删除内容中的图片
                for (String url : fileIdList) {
                    ossService.delete(url);
                }
            } catch (Exception e) {
                // 图片删除失败不影响博客删除
                log.error("delBlog success but delete blog fileId failed, blogIds:{}", ids);
            }
        }
        return ResultModel.ok();
    }
}

