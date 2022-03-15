package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.LoginDto;

import java.text.ParseException;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface AdminService extends IService<Admin> {

    Admin checkLogin(LoginDto loginDto);

    Response getAdminByPage(AdminDto adminDto) throws ParseException;

    Response addAdmin(AdminDto adminDto);
}
