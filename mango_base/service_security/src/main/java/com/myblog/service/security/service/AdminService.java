package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.security.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.LoginDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto;

import java.text.ParseException;
import java.util.Set;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface AdminService extends IService<Admin>, ServiceConvertHandler<Admin, AdminDto> {

    Admin checkLogin(LoginDto loginDto) throws Exception;

    Response getAdminByPage(AdminDto adminDto) throws Exception;

    Response addAdmin(AdminDto adminDto) throws Exception;

    Response delAdmin(Set<String> ids) throws Exception;

    Response editAdminFromCenter(AdminDto adminDto) throws InstantiationException, IllegalAccessException;
}
