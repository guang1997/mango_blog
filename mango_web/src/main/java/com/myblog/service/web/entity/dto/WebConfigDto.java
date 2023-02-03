package com.myblog.service.web.entity.dto;

import java.util.List;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class WebConfigDto extends BaseDto {

    private String name;

    private String summary;

    private String author;

    private String recordNum;

    private String github;

    private String gitee;

    private List<String> rollingSentences;
}
