package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleDto extends BaseDto {

    private String roleName;

    private String summary;

}
