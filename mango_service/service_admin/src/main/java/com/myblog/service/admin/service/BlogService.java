package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Set;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
public interface BlogService extends IService<Blog>, ServiceConvertHandler<Blog, BlogDto> {

    int getBlogCount();

    Response getBlogCountByTag();

    Response getBlogCountByBlogSort();

    Response getBlogContributeCount() throws Exception;

    Response getBlogByPage(BlogDto blogDto) throws Exception;

    Response addBlog(BlogDto blogDto) throws Exception;

    Response editBlog(BlogDto blogDto);

    Response delBlog(Set<String> ids);
}
