package com.myblog.service.security.entity.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PassAndEmailDto {

    @NotNull(message = "旧密码不能为空", groups = {PassAndEmailGroup.EditPass.class})
    private String oldPass;

    @NotNull(message = "新密码不能为空", groups = {PassAndEmailGroup.EditPass.class})
    private String newPass;

    @NotNull(message = "密码不能为空", groups = {PassAndEmailGroup.EditEmail.class})
    private String pass;

    @NotNull(message = "邮箱不能为空", groups = {PassAndEmailGroup.EditEmail.class})
    private String email;

    private String code;

    private String oldEmail;

    public interface PassAndEmailGroup {
        interface EditPass{}
        interface EditEmail{}
    }
}
