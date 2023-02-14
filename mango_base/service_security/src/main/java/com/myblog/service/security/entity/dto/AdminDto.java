package com.myblog.service.security.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ToString(exclude = "password")
public class AdminDto extends BaseReqDto {

    @NotNull(message = "用户名不能为空", groups = {AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    @Size(min = 0, max = 64, message = "用户名长度不能超过64",
        groups = {AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    @Pattern(message = "用户名只能是英文或者数字", regexp = "^[a-z0-9A-Z]+$",
        groups = {AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    private String username;

    private String password;

    private Integer gender;

    private String avatar;

    @NotNull(message = "邮箱不能为空", groups = {AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    private String email;

    @NotNull(message = "手机不能为空", groups = {AdminGroup.EditAdminFromCenter.class, AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    private String phone;

    private Integer loginCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date lastLoginTime;

    private String lastLoginIp;

    @NotNull(message = "昵称不能为空", groups = {AdminGroup.EditAdminFromCenter.class, AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    @Size(min = 0, max = 64, message = "昵称长度不能超过64",
        groups = {AdminGroup.EditAdminFromCenter.class, AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
    @Pattern(message = "昵称只能是英文或者数字", regexp = "^[a-z0-9A-Z\\u4e00-\\u9fa5]+$",
        groups = {AdminGroup.EditAdminFromCenter.class, AdminGroup.EditAdmin.class, AdminGroup.AddAdmin.class})
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

    public interface AdminGroup {
        interface EditAdminFromCenter {}

        interface EditAdmin{}

        interface AddAdmin{}
    }
}
