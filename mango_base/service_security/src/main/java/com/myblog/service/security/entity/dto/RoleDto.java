package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleDto extends BaseDto {

    @NotNull(message = "角色名不能为空")
    private String roleName;

    private String summary;

    private List<MenuDto> menus;

    private Boolean searchAll;

    /**
     * 模糊查询用
     */
    private String blurry;

    @NotNull(message = "角色级别不能为空")
    private Integer level;

    private List<String> createTimes;
}
