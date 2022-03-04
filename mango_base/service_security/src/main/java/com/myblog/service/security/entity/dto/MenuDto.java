package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class MenuDto extends BaseDto {

    private String id;

    private String pid;

    private boolean hidden;

    private String redirect;

    private boolean alwaysShow;

    private String name;

    private String component;

    private String path;

    private String title;

    private Boolean menuLevel;

    private String icon;

    private Integer sort;

    private String menuType;

    private List<MenuDto> children;

    public Boolean getHasChildren() {
        return children != null && children.size() > 0;
    }
}
