package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.User;
import com.myblog.service.admin.entity.dto.UserDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.security.entity.dto.AdminDto;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-18
 */
public interface UserService extends IService<User>, ServiceConvertHandler<User, UserDto> {

    Response getUserByPage(UserDto userDto) throws Exception;

    Response editUser(UserDto userDto);
}
