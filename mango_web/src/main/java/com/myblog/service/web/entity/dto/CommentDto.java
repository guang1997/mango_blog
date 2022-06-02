package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class CommentDto extends BaseDto {

    private String parentId;

    private String userId;

    private String avatar;

    private String nickname;

    /**
     * 被评论人昵称
     */
    private String toNickname;

    private String content;

    private String source;

    private Integer type;

    private String blogId;

    private Integer status;

    private List<CommentDto> children;

    private List<String> createTimes;

    private Boolean isLiked;

    private Integer likeCount;

    private UniqueKeyDto screenInformation;

    /**
     * 被评论人昵称
     */
    private String answerNickname;

    private Boolean queryChildren;

    private String browserFinger;
}
