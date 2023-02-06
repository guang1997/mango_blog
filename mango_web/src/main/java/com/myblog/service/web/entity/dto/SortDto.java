package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

@Data
public class SortDto extends BaseReqDto {

    private String sortName;

    private String summary;

    private Integer clickCount;

    private Integer blogNum;

    private Boolean queryAll;
}
