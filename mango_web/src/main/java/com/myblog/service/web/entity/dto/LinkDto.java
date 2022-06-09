package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class LinkDto extends BaseDto {

    private String title;

    private String summary;

    private String url;

    private Integer linkStatus;


}
