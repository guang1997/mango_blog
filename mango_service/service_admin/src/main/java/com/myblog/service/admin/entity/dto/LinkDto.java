package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class LinkDto extends BaseDto {

    private String title;

    private String summary;

    private String url;

    private Integer linkStatus;

    private String userId;

    private String adminId;

    private String fileId;

    private String blurry;
}
