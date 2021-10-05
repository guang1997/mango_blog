package com.myblog.service.security.config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.exception.LoginException;
import com.myblog.service.security.config.entity.Audience;
import com.myblog.service.security.config.entity.AuthUser;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 要实现UserDetailsService接口，这个接口是security提供的
 */
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private Audience audience;

    /**
     * 通过账号查找用户、角色的信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws LoginException {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq(DbConstants.Admin.userName, username);
        Admin admin = adminService.getOne(adminQueryWrapper);
        if (admin == null) {
            return null;
        }else {
            //查找角色
            List<String> roles =  roleService.getRolesByUserName(username);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return new AuthUser(admin.getId(),
                    admin.getUserName(),
                    admin.getPassWord(),
                    null,
                    admin.getAvatar(),
                    audience.getExpiresSecond(),
                    authorities);
        }
    }
}
