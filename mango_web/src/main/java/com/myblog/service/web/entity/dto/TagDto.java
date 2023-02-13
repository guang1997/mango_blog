package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import java.util.List;

@Data
public class TagDto extends BaseReqDto {

    private String tagName;

    private String summary;

    private Integer clickCount;

    private List<String> createTimes;

    private Boolean queryAll;

    private Boolean queryLatest;
}
