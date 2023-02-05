package com.myblog.service.admin.entity.dto;

import com.myblog.service.admin.entity.Dict;
import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DictDetailDto extends BaseReqDto {
    @NotNull(message = "字典id不能为空", groups = {DictDetailValidGroup.Add.class, DictDetailValidGroup.Update.class})
    private String dictId;

    @NotNull(message = "字典名称不能为空", groups = DictDetailValidGroup.Query.class)
    private String dictName;

    @NotNull(message = "字典标签不能为空", groups = {DictDetailValidGroup.Add.class, DictDetailValidGroup.Update.class})
    private String dictLabel;

    @NotNull(message = "字典值不能为空", groups = {DictDetailValidGroup.Add.class, DictDetailValidGroup.Update.class})
    private String dictValue;

    private String cssClass;

    private String listClass;

    private String summary;

    private Integer sort;

    public interface DictDetailValidGroup {
        interface Add{}
        interface Del{}
        interface Query{}
        interface Update{}
    }
}
