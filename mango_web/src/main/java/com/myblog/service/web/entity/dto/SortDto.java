package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SortDto extends BaseDto {

    private String sortName;

    private String summary;

    private Integer clickCount;

    private Integer blogNum;

    private Boolean queryAll;
}
