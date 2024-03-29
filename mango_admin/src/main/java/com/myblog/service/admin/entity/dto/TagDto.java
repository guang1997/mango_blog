package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TagDto extends BaseReqDto {

    @NotNull(message = "标签名称不能为空")
    private String tagName;

    private String summary;

    private Integer clickCount;

    private List<String> createTimes;

    private Boolean queryAll;
}
