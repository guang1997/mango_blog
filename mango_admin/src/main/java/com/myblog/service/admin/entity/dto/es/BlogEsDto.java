package com.myblog.service.admin.entity.dto.es;

import com.myblog.service.base.entity.es.BaseEsEntity;
import lombok.Data;

@Data
public class BlogEsDto extends BaseEsEntity {

    private String title;

    private String summary;

    private String content;
}
