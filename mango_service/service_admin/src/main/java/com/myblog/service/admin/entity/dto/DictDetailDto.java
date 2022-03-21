package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

@Data
public class DictDetailDto extends BaseDto {
    private String dictId;

    private String dictLabel;

    private String dictValue;

    private String cssClass;

    private String listClass;

    private Boolean isDefault;

    private String createAdminId;

    private String updateAdminId;

    private String remark;

    private Integer sort;
}
