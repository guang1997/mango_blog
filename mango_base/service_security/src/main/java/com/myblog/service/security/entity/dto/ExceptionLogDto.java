package com.myblog.service.security.entity.dto;

import java.util.List;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

@Data
public class ExceptionLogDto extends BaseReqDto {

    private String exceptionMessage;

    private String ip;

    private String ipSource;

    private String method;

    private String params;

    private String exceptionDetails;

    private List<String> createTimes;
}
