package com.myblog.service.admin.entity.dto;

import com.myblog.service.admin.entity.Tag;
import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;
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

    private String blogSortId;

    private String sortName;

    private Integer level;

    private Integer sort;

    private Integer openComment;

    private String tagId;

    private List<Tag> blogTags;
}
