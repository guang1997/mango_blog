package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class DictDto extends BaseDto {
    private String dictName;

    private String summary;

    private Integer sort;

    private String blurry;
}
