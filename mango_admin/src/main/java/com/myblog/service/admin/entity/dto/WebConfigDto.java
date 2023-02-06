package com.myblog.service.admin.entity.dto;

import java.util.List;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

@Data
public class WebConfigDto extends BaseReqDto {

    private String name;

    private String summary;

    private String author;

    private String recordNum;

    private String github;

    private String gitee;

    private List<String> rollingSentences;

    private Boolean deleteSentence;
}
