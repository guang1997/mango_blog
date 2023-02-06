package com.myblog.service.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WebVisitDto extends BaseReqDto {
    private String userId;

    private String ip;

    private String os;

    private String browser;

    private String ipSource;

    private String behavior;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date requestTime;

    public List<String> createTimes;
}
