package com.myblog.service.security.entity.dto;

import com.myblog.service.base.entity.dto.BaseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminDto extends BaseDto {

    private String username;

    private String password;

    private String gender;

    private String avatar;

    private String id;

    private Date createTime;

    private String email;

    private String mobile;

    private Integer loginCount;

    private Date lastLoginTime;

    private String lastLoginIp;

    private String nickname;

    private String qqNumber;

    private String weChat;

    private Integer enabled;

    private List<RoleDto> roleDtos;
    /**
     * 模糊查询用
     */
    private String blurry;

    private List<String> createTimes;
}
