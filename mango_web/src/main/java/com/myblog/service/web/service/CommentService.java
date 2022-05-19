package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.CommentDto;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
public interface CommentService extends IService<Comment>, ServiceConvertHandler<Comment, CommentDto> {

    Response getCommentByPage(CommentDto commentDto) throws Exception;
}
