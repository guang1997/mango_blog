package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DictDto extends BaseDto {

    @NotNull(message = "字典名称不能为空")
    private String dictName;

    private String summary;

    private Integer sort;

    private String blurry;

    private List<DictDto> dictDetails;
}
