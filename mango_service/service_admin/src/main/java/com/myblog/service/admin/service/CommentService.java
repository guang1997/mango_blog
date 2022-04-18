package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.CommentDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Set;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-11
 */
public interface CommentService extends IService<Comment>, ServiceConvertHandler<Comment, CommentDto> {

    Response getCommentByPage(CommentDto commentDto) throws Exception;

    Response delComment(Set<String> ids);

    int getCommentCount(int status);
}
