package com.myblog.service.admin.entity.dto.es;

import lombok.Data;

@Data
public class BlogEsDto {

    private String id;

    private String title;

    private String summary;

    private String content;
}
