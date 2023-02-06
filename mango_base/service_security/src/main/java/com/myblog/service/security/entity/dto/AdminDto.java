package com.myblog.service.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(exclude = "password")
public class AdminDto extends BaseReqDto {

    private String username;

    private String password;

    private Integer gender;

    private String avatar;

    private String email;

    private String phone;

    private Integer loginCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date lastLoginTime;

    private String lastLoginIp;

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
