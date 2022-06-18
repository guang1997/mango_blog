package com.myblog.service.admin.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(exclude = "password")
public class UserDto extends BaseDto {

    private static final long serialVersionUID=1L;

    private String username;

    private String password;

    private Integer gender;

    private String avatar;

    private Date birthday;

    private String email;

    private String mobile;

    private Integer loginCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer status;

    private String nickname;

    private String qqNumber;

    private String weChat;

    private Integer commentStatus;

    private String blurry;

    private List<String> lastLoginTimes;
}
