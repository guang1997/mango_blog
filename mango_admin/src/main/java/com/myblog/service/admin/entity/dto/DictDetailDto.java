package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DictDetailDto extends BaseReqDto {
    @NotNull(message = "字典id不能为空")
    private String dictId;

    private String dictName;

    @NotNull(message = "字典标签不能为空")
    private String dictLabel;

    @NotNull(message = "字典值不能为空")
    private String dictValue;

    private String cssClass;

    private String listClass;

    private String summary;

    private Integer sort;
}
