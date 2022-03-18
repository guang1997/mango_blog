package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class TagDto extends BaseDto {

    private String tagName;

    private String content;

    private Integer clickCount;

    private Integer sort;

    private List<String> createTimes;
}
