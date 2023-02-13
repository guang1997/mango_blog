package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-27
 */
public interface UserService extends IService<User>, ServiceConvertHandler<User, UserDto> {

    UserDto doLogin(UserDto userDto, HttpServletRequest request) throws Exception;

    UserDto getUser(UserDto userDto) throws Exception;
}
