package com.myblog.service.web.mapper;

import com.myblog.service.web.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
public interface CommentMapper extends BaseMapper<Comment> {

    int updateLikeCount(@Param("likeCount") int likeCount, @Param("parentId") String parentId);
}
