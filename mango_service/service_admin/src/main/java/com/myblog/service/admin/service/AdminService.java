package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.vo.LoginVo;
import com.myblog.service.base.common.Response;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
public interface AdminService extends IService<Admin> {

    Response doLogin(LoginVo loginVo);
}
