package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.exception.BusinessException;
import com.myblog.service.base.util.CheckUtils;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.web.controller.LoginController;
import com.myblog.service.web.entity.User;
import com.myblog.service.web.entity.dto.UserDto;
import com.myblog.service.web.mapper.UserMapper;
import com.myblog.service.web.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

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

    private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${web.username.prefix:admin}")
    private String prefix;
    /**
     * 登陆用户，如果用户不存在那么新增一个，目前只支持邮箱登陆
     *
     * @param userDto
     * @return
     */
    @Override
    public UserDto doLogin(UserDto userDto, HttpServletRequest request) throws Exception {
        userDto.setUsername(userDto.getNickname());
        // 根据用户名查询是否存在该用户，暂时不校验密码
        QueryWrapper<User> userWrapper = new QueryWrapper<>();

        // 登陆过的用户更新信息
        userWrapper.eq(DbConstants.User.EMAIL, userDto.getEmail());
        userWrapper.eq(DbConstants.User.STATUS, Constants.CommonStatus.ENABLED);
        User dbUser = baseMapper.selectOne(userWrapper);
        if (Objects.nonNull(dbUser)) {
            dbUser.setLoginCount(dbUser.getLoginCount() + 1);
            dbUser.setUsername(userDto.getUsername());
            dbUser.setNickname(userDto.getNickname());
            dbUser.setLastLoginIp(IpUtils.getIpAddr(request));
            baseMapper.updateById(dbUser);
            return this.toDto(dbUser, UserDto.class);
        }
        // 第一次登陆的保存到数据库
        User user = this.toDb(userDto, User.class);
        user.setPassword(passwordEncoder.encode(prefix + ThreadSafeDateFormat.format(new Date(), ThreadSafeDateFormat.YEAR)));
        user.setLoginCount(1);
        user.setStatus(Constants.CommonStatus.ENABLED);
        user.setCommentStatus(Constants.CommonStatus.ENABLED);
        user.setLastLoginIp(IpUtils.getIpAddr(request));
        user.setLastLoginTime(new Date());
        if (baseMapper.insert(user) < 1) {
            LOGGER.error("login failed by insert user:{} to db", user);
            throw new BusinessException("登陆失败");
        }
        dbUser = baseMapper.selectOne(userWrapper);
        return this.toDto(dbUser, UserDto.class);
    }

    @Override
    public UserDto getUser(UserDto userDto) throws Exception {
        User user = baseMapper.selectById(userDto.getId());
        if (Objects.isNull(user)) {
            throw new BusinessException("获取用户信息失败");
        }
        return this.toDto(user, UserDto.class);
    }
}
