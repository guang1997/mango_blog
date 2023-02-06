package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SortDto extends BaseReqDto {

    @NotNull(message = "分类名称不能为空")
    private String sortName;

    private String summary;

    private Integer clickCount;

    private List<String> createTimes;

    private Boolean queryAll;
}
