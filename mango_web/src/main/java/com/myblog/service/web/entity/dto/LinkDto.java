package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

@Data
public class LinkDto extends BaseReqDto {

    private String title;

    private String summary;

    private String url;

    private Integer linkStatus;

    private String code;

    private String email;
}
