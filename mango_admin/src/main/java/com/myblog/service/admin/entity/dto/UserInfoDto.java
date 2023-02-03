package com.myblog.service.admin.entity.dto;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class UserInfoDto {

    private Set<String> roles;

    private String token;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private String qqNumber;

    private String weChat;

    private String id;

    private String avatar;

    private Integer gender;

    private List<String> menuButtons;
}
