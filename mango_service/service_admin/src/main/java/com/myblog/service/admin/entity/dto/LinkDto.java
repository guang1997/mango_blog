package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LinkDto extends BaseDto {

    @NotNull(message = "友链名称不能为空")
    private String title;

    private String summary;

    @NotNull(message = "友链路径不能为空")
    private String url;

    private Integer linkStatus;

    private String fileId;

    private String blurry;
}
