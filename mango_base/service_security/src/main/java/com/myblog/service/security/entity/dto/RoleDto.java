package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto extends BaseDto {

    private String roleName;

    private String summary;

    private List<MenuDto> menus;

    private Boolean searchAll;

    /**
     * 模糊查询用
     */
    private String blurry;

    private Integer level;

    private List<String> createTimes;
}
