package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;


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

    private String tagId;
}
