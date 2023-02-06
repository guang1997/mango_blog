package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Data
public class MenuDto extends BaseReqDto {

    private String pid;

    private Boolean hidden;

    private String permission;

    private String name;

    private String component;

    private String path;

    @NotNull(message = "菜单名不能为空")
    private String title;

    private String icon;

    private Integer sort;

    private Integer menuType;

    private List<MenuDto> children;

    private Integer subCount;

    private Meta meta;

    /**
     * 是否为子节点
     * @return
     */
    public Boolean getLeaf() {
        return Objects.nonNull(subCount) && subCount <= 0;
    }

    public String getLabel() {
        return title;
    }

    public Boolean getHasChildren() {
        return Objects.nonNull(subCount) && subCount > 0;
    }
}
