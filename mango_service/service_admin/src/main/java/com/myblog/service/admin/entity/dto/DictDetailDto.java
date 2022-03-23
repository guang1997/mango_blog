package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class DictDetailDto extends BaseDto {
    private String dictId;

    private String dictName;

    private String dictLabel;

    private String dictValue;

    private String cssClass;

    private String listClass;

    private String summary;

    private Integer sort;
}
