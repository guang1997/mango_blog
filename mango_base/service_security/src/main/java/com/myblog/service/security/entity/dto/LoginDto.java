package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class LoginDto extends BaseDto {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    private Boolean rememberMe;

    private String avatar;

    private Long expiresSecond;

    private List<Role> roles;
}
