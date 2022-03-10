package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.util.CheckUtils;
import com.myblog.service.security.config.entity.vo.AdminVO;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.mapper.AdminMapper;
import com.myblog.service.security.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public Admin checkLogin(AdminVO adminVO) {
        String username = adminVO.getUsername();
        String password = adminVO.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            LOGGER.error("admin login failed, userName:{}", username);
            return null;
        }
        boolean isEmail = CheckUtils.checkEmails(username);
        boolean isMobile = CheckUtils.checkMobile(username);
        // 根据用户名查询是否存在该用户
        QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
        if (isEmail) {
            adminWrapper.eq(DbConstants.Admin.EMAIL, username);
        } else if (isMobile) {
            adminWrapper.eq(DbConstants.Admin.MOBILE, username);
        } else {
            adminWrapper.eq(DbConstants.Admin.USERNAME, username);
        }
        adminWrapper.eq(DbConstants.Base.IS_DELETED, "0");
        Admin admin = baseMapper.selectOne(adminWrapper);
        if (admin == null) {
            LOGGER.error("admin login failed, cannot find admin by userName:{}", username);
            return null;
        }
        // 对密码进行动态加盐处理
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matchPassword = encoder.matches(password, admin.getPassword());
        if (!matchPassword) {
            LOGGER.error("admin login failed, password is error");
            return null;
        }

        return admin;
    }
}
