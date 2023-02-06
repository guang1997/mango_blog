package com.myblog.service.admin.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DictDto extends BaseReqDto {

    @NotNull(message = "字典名称不能为空")
    private String dictName;

    private String summary;

    private Integer sort;

    private String blurry;

    private List<DictDto> dictDetails;
}
