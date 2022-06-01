package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class CommentDto extends BaseDto {

    private String parentId;

    private String userId;

    private String ip;

    private String avatar;

    private String nickname;

    /**
     * 被评论人昵称
     */
    private String answerNickname;

    private String content;

    private String source;

    private Integer type;

    private String blogId;

    private Integer status;

    private List<CommentDto> children;

    private List<String> createTimes;
}
