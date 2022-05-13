package com.myblog.service.admin.mapper;

import com.myblog.service.admin.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-16
 */
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    @MapKey("tag_id")
    List<Map<String, Object>> getBlogCountByTag();
}
