package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.web.entity.Comment;
import com.myblog.service.web.entity.Sort;
import com.myblog.service.web.entity.Tag;
import lombok.Data;

import java.util.List;


@Data
public class BlogDto extends BaseDto {

    private String title;

    private String summary;

    private String content;

    private Integer clickCount;

    private Integer likeCount;

    private String fileId;

    private String adminId;

    private String author;

    private Sort sort;

    private List<Tag> tags;

    private String blogSortId;

    private String tagId;

    private List<Comment> comments;

    private Boolean liked;

    private String userId;

    private UniqueKeyDto screenInformation;

    private String browserFinger;
}
