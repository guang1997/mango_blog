package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class DictDto extends BaseDto {
    private String dictName;

    private String summary;

    private Integer sort;

    private String blurry;

    private List<DictDto> dictDetails;
}
