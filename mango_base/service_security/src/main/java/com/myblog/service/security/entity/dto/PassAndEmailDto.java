package com.myblog.service.security.entity.dto;

import lombok.Data;

@Data
public class PassAndEmailDto {

    private String oldPass;

    private String newPass;

    private String pass;

    private String email;

    private String code;

    private String oldEmail;
}
