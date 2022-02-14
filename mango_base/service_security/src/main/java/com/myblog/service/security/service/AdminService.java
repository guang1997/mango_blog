package com.myblog.service.security.service;

import com.myblog.service.security.config.entity.vo.AdminVO;
import com.myblog.service.security.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface AdminService extends IService<Admin> {

    Admin checkLogin(AdminVO adminVO);
}
