package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class LoginDto extends BaseDto {

    private String username;

    private String password;

    private Boolean rememberMe;

    private String avatar;

    private Long expiresSecond;

    private List<Role> roles;
}
