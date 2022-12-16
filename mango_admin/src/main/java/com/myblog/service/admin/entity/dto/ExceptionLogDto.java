package com.myblog.service.admin.entity.dto;

import java.util.List;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class ExceptionLogDto extends BaseDto {

    private String exceptionMessage;

    private String ip;

    private String ipSource;

    private String method;

    private String params;

    private List<String> createTimes;
}
