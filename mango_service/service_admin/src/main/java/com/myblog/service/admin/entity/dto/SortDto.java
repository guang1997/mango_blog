package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SortDto extends BaseDto {

    @NotNull(message = "分类名称不能为空")
    private String sortName;

    private String summary;

    private Integer clickCount;

    @NotNull(message = "分类级别不能为空")
    private Integer sortLevel;

    private List<String> createTimes;

    private Boolean queryAll;
}
