package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class MenuDto extends BaseDto {

    private String pid;

    private Boolean hidden;

    private String permission;

    private String name;

    private String component;

    private String path;

    private String title;

    private String icon;

    private Integer sort;

    private Integer menuType;

    private List<MenuDto> children;

    private Integer subCount;

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return title;
    }

    public Boolean getHasChildren() {
        return subCount > 0;
    }
}
