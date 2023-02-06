package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(exclude = "password")
public class UserDto extends BaseReqDto {

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

    private Integer commentStatus;

    private String code;
}
