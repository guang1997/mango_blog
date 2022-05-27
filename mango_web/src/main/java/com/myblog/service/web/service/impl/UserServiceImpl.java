package com.myblog.service.web.service.impl;

import com.myblog.service.web.entity.User;
import com.myblog.service.web.mapper.UserMapper;
import com.myblog.service.web.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
