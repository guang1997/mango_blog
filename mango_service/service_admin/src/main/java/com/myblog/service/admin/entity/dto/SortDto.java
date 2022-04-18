package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class SortDto extends BaseDto {

    private String sortName;

    private String summary;

    private Integer clickCount;

    private Integer sortLevel;

    private List<String> createTimes;

    private Boolean queryAll;
}
