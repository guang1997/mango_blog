package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.CheckUtils;
import com.myblog.service.base.util.QueryWrapperDecorator;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.LoginDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.mapper.AdminMapper;
import com.myblog.service.security.mapper.RoleMapper;
import com.myblog.service.security.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Admin checkLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
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

    /**
     * 分页查询管理员信息
     * @param adminDto
     * @return
     */
    @Override
    public Response getAdminByPage(AdminDto adminDto) throws ParseException {
        Response response = Response.ok();
        int page = 1;
        int size = 10;
        if (!Objects.isNull(adminDto.getPage())) page = adminDto.getPage();
        if (!Objects.isNull(adminDto.getSize())) size = adminDto.getSize();
        Page<Admin> adminPage = new Page<>(page, size);

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        if (StringUtils.isNotBlank(adminDto.getBlurry())) {
            queryWrapper.like(DbConstants.Admin.USERNAME, adminDto.getBlurry())
                    .or()
                    .like(DbConstants.Admin.NICKNAME, adminDto.getBlurry());
        }
        if (StringUtils.isNotBlank(adminDto.getGender())) {
            queryWrapper.eq(DbConstants.Admin.GENDER, adminDto.getGender());
        }
        if (!Objects.isNull(adminDto.getEnabled())) {
            queryWrapper.eq(DbConstants.Admin.ENABLED, adminDto.getEnabled());
        }
        if (!CollectionUtils.isEmpty(adminDto.getLastLoginTimes()) && Objects.equals(2, adminDto.getLastLoginTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(adminDto.getLastLoginTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(adminDto.getLastLoginTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Admin.LAST_LOGIN_TIME, beginDate, endDate);
        }

        baseMapper.selectPage(adminPage, queryWrapper);
        List<AdminDto> adminDtos = toDto(adminPage.getRecords());

        response.data(Constants.ReplyField.DATA, adminDtos);
        response.data(Constants.ReplyField.TOTAL, adminPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 创建admin
     * @param adminDto
     * @return
     */
    @Override
    public Response addAdmin(AdminDto adminDto) {
        return null;
    }

    private List<AdminDto> toDto(List<Admin> admins) {
        List<AdminDto> adminDtos = new ArrayList<>();
        for (Admin admin : admins) {
            AdminDto adminDto = new AdminDto();
            BeanUtil.copyProperties(admin, adminDto);
            adminDto.setId(admin.getId());
            adminDto.setCreateTime(admin.getCreateTime());
            // 查询角色信息
            List<RoleDto> roleDtos = toRoleDto(roleMapper.getRolesByUserId(admin.getId()));
            adminDto.setRoles(roleDtos);
            adminDtos.add(adminDto);
        }
        return adminDtos;
    }

    private List<RoleDto> toRoleDto(List<Role> roleList) {
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roleList) {
            RoleDto roleDto = new RoleDto();
            BeanUtil.copyProperties(role, roleDto);
            roleDto.setId(role.getId());
            roleDto.setCreateTime(role.getCreateTime());
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }
}
