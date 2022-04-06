package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.*;
import com.myblog.service.base.util.*;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.LoginDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto;
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
import java.util.*;

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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Admin checkLogin(LoginDto loginDto) throws Exception{
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
            adminWrapper.eq(DbConstants.Admin.PHONE, username);
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
        boolean matchPassword = passwordEncoder.matches(password, admin.getPassword());
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
    public Response getAdminByPage(AdminDto adminDto) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 10;
        if (Objects.nonNull(adminDto.getPage())) page = adminDto.getPage();
        if (Objects.nonNull(adminDto.getSize())) size = adminDto.getSize();
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
        if (Objects.nonNull(adminDto.getEnabled())) {
            queryWrapper.eq(DbConstants.Admin.ENABLED, adminDto.getEnabled());
        }
        if (!CollectionUtils.isEmpty(adminDto.getLastLoginTimes()) && Objects.equals(2, adminDto.getLastLoginTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(adminDto.getLastLoginTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(adminDto.getLastLoginTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Admin.LAST_LOGIN_TIME, beginDate, endDate);
        }

        baseMapper.selectPage(adminPage, queryWrapper);
        List<AdminDto> adminDtos = this.toDtoList(adminPage.getRecords(), AdminDto.class);

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

        return Response.ok();
    }

    /**
     * 删除管理员
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public Response delAdmin(Set<String> ids) throws Exception{
        for (String id : ids) {
            Admin admin = baseMapper.selectById(id);
            // 清理缓存信息
            redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + admin.getUsername());
            baseMapper.deleteById(id);
        }
        return Response.ok();
    }

    /**
     * 从个人中心页面更改管理员信息
     * @param adminDto
     * @return
     */
    @Override
    public Response editAdminFromCenter(AdminDto adminDto) throws InstantiationException, IllegalAccessException {
        Admin admin = baseMapper.selectById(adminDto.getId());

        QueryWrapper<Admin> phoneQueryWrapper = new QueryWrapper<>();
        phoneQueryWrapper.eq(DbConstants.Admin.PHONE, adminDto.getPhone());
        Admin adminByPhone = baseMapper.selectOne(phoneQueryWrapper);
        if (Objects.nonNull(adminByPhone) && !Objects.equals(admin.getId(), adminByPhone.getId())) {
            LOGGER.error("editAdminFromCenter failed, phone is already exist, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED_BY_PHONE_EXIST);
        }

        QueryWrapper<Admin> qqNumberQueryWrapper = new QueryWrapper<>();
        qqNumberQueryWrapper.eq(DbConstants.Admin.QQ_NUMBER, adminDto.getQqNumber());
        Admin adminByQQNumber = baseMapper.selectOne(qqNumberQueryWrapper);
        if (Objects.nonNull(adminByQQNumber) && !Objects.equals(admin.getId(), adminByQQNumber.getId())) {
            LOGGER.error("editAdminFromCenter failed, qqNumber is already exist, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED_BY_QQ_NUMBER_EXIST);
        }
        QueryWrapper<Admin> weChatQueryWrapper = new QueryWrapper<>();
        weChatQueryWrapper.eq(DbConstants.Admin.WE_CHAT, adminDto.getWeChat());
        Admin adminByWeChat = baseMapper.selectOne(weChatQueryWrapper);
        if (Objects.nonNull(adminByWeChat) && !Objects.equals(admin.getId(), adminByWeChat.getId())) {
            LOGGER.error("editAdminFromCenter failed, weChat is already exist, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED_BY_WE_CHAT_EXIST);
        }
        if (baseMapper.updateById(this.toDb(adminDto, Admin.class)) < 1) {
            LOGGER.error("editAdminFromCenter failed by unknown error, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    @Override
    public void setDtoExtraProperties(Admin admin, AdminDto adminDto) {
        adminDto.setId(admin.getId());
        adminDto.setCreateTime(admin.getCreateTime());
        adminDto.setUpdateTime(admin.getUpdateTime());
        // 查询角色信息
        List<RoleDto> roleDtos = toRoleDto(roleMapper.getRolesByUserId(admin.getId()));
        adminDto.setRoles(roleDtos);
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
