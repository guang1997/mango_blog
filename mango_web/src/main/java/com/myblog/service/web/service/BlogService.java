package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.base.handler.es.entity.BlogEsDto;
import com.myblog.service.web.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.ArchiveDto;
import com.myblog.service.web.entity.dto.BlogDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-17
 */
public interface BlogService extends IService<Blog>, ServiceConvertHandler<Blog, BlogDto> {

    Map<String, Object> getBlogByPage(BlogDto blogDto) throws Exception;

    List<BlogDto> getBlogBySortId(BlogDto blogDto) throws Exception;

    List<BlogDto> getBlogByTagId(BlogDto blogDto) throws Exception;

    BlogDto getBlogById(BlogDto blogDto, HttpServletRequest request) throws Exception;

    Map<String, Object> getPrevNextBlog(BlogDto blogDto) throws Exception;

    Map<String, Object> getArchives(ArchiveDto archiveDto) throws Exception;

    List<ArchiveDto> initArchives(ArchiveDto archiveDto);

    List<BlogEsDto> getBlogByKeyword(BlogDto blogDto) throws Exception;
}
