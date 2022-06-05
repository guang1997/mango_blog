package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.ArchiveDto;
import com.myblog.service.web.entity.dto.BlogDto;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-17
 */
public interface BlogService extends IService<Blog>, ServiceConvertHandler<Blog, BlogDto> {

    Response getBlogByPage(BlogDto blogDto) throws Exception;

    Response getBlogBySortId(BlogDto blogDto) throws Exception;

    Response getBlogByTagId(BlogDto blogDto) throws Exception;

    Response getBlogById(BlogDto blogDto, HttpServletRequest request) throws Exception;

    Response getPrevNextBlog(BlogDto blogDto) throws Exception;

    Response getArchives(ArchiveDto archiveDto) throws Exception;
}
