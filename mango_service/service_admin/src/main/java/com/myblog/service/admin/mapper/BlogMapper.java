package com.myblog.service.admin.mapper;

import com.myblog.service.admin.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.service.admin.entity.dto.BlogDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
public interface BlogMapper extends BaseMapper<Blog> {

    @MapKey("sortId")
    List<Map<String, Object>> getBlogCountByBlogSort();

    @MapKey("date")
    List<Map<String, Object>> getBlogContributeCount(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Blog> selectBlogByRequest(BlogDto blogDto);

    int selectBlogCountByRequest(BlogDto blogDto);
}
