package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.List;
import java.util.Map;
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

    List<Map<String, Object>> getBlogCountByTag();

    List<Map<String, Object>> getBlogCountByBlogSort();

    Map<String, Object> getBlogContributeCount() throws Exception;

    Map<String, Object> getBlogByPage(BlogDto blogDto) throws Exception;

    Boolean addBlog(BlogDto blogDto) throws Exception;

    Boolean editBlog(BlogDto blogDto) throws Exception;

    List<String> delBlog(Set<String> ids) throws Exception;
}
