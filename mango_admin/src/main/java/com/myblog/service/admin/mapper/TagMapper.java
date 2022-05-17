package com.myblog.service.admin.mapper;

import com.myblog.service.admin.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
public interface TagMapper extends BaseMapper<Tag> {

    List<String> selectBlogIdsByTagId(@Param("tagId") String tagId);
}
