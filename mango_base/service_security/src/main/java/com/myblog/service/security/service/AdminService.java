package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.security.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.LoginDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
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

    Map<String, Object> getAdminByPage(AdminDto adminDto) throws Exception;

    Boolean addAdmin(AdminDto adminDto) throws Exception;

    List<String> delAdmin(Set<String> ids);

    Response editAdminFromCenter(AdminDto adminDto) throws Exception;

    Response updateEmail(PassAndEmailDto passAndEmailDto) throws Exception;

    Response updatePass(PassAndEmailDto passAndEmailDto) throws Exception;

    Boolean editAdmin(AdminDto adminDto) throws Exception;

    int getAdminCount();

}
