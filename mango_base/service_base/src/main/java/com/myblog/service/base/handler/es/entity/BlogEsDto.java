package com.myblog.service.base.handler.es.entity;

import com.myblog.service.base.entity.es.BaseEsEntity;
import lombok.Data;

@Data
public class BlogEsDto extends BaseEsEntity {

    private String title;

    private String summary;

    private String content;
}
