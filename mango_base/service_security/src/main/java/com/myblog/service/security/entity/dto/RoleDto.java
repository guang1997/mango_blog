package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto extends BaseDto {

    private String roleName;

    private String summary;

    private List<MenuDto> menus;
}
