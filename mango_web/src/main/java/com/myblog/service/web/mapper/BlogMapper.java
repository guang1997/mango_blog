package com.myblog.service.web.mapper;

import com.myblog.service.web.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.service.web.entity.Tag;
import com.myblog.service.web.entity.dto.BlogDto;
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
 * @since 2022-05-17
 */
public interface BlogMapper extends BaseMapper<Blog> {

    @MapKey("blog_sort_id")
    List<Map<String, Object>> selectCountGroupByBlogSort();

    List<Tag> selectTagByBlogId(@Param("blogs") List<Blog> blogs);

    Integer changeLike(@Param("blogId") String blogId, @Param("likeCount") int likeCount);
}
