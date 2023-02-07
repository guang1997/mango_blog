package com.myblog.service.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString(exclude = "password")
public class AdminDto extends BaseReqDto {

    @NotNull(message = "用户名不能为空")
    private String username;

    private String password;

    private Integer gender;

    private String avatar;

    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "手机不能为空")
    private String phone;

    private Integer loginCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date lastLoginTime;

    private String lastLoginIp;

    @NotNull(message = "昵称不能为空")
    private String nickname;

    private String qqNumber;

    private String weChat;

    private Integer enabled;

    private List<RoleDto> roles;
    /**
     * 模糊查询用
     */
    private String blurry;

    private List<String> lastLoginTimes;

    private Boolean changeEnabled;
}
