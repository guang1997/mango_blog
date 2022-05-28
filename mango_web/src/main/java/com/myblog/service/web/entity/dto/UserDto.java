package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends BaseDto {

    private String username;

    private String password;

    private Integer gender;

    private String avatar;

    private Date birthday;

    private String email;

    private String mobile;

    private Integer loginCount;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer status;

    private String nickname;

    private String qqNumber;

    private String weChat;

    private Boolean commentStatus;

    private Boolean userTag;

    private String code;
}
