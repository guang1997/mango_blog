package com.myblog.service.web.entity.dto;

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


    private String sortName;

    private Integer sort;

    private List<TagDto> tags;

    private String blogSortId;
}
