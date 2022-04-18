package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.*;
import com.myblog.service.base.util.*;
import com.myblog.service.security.config.entity.MySecurityProperties;
import com.myblog.service.security.config.util.RsaUtils;
import com.myblog.service.security.config.util.SecurityUtils;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.RoleAdmin;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.LoginDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.mapper.AdminMapper;
import com.myblog.service.security.mapper.RoleAdminMapper;
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
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private MySecurityProperties mySecurityProperties;

    @Autowired
    private RoleAdminMapper roleAdminMapper;

    @Override
    public Admin checkLogin(LoginDto loginDto) throws Exception {
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
     *
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
        if (Objects.nonNull(adminDto.getGender())) {
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
     *
     * @param adminDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addAdmin(AdminDto adminDto) throws Exception {
        // 校验管理员是否已经存在
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Admin.USERNAME, adminDto.getUsername());
        queryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        List<Admin> admins = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(admins)) {
            LOGGER.error("addAdmin failed, admin already exist in db, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED).message("保存失败，用户名已存在");
        }
        Admin admin = this.toDb(adminDto, Admin.class);
        // 给管理员设置默认密码，规则为用户名+yyyy
        admin.setPassword(passwordEncoder.encode(admin.getUsername() + ThreadSafeDateFormat.format(new Date(), ThreadSafeDateFormat.YEAR)));
        admin.setUpdateTime(new Date());
        admin.setCreateTime(new Date());
        admin.setLoginCount(0);
        admin.setLastLoginIp(null);
        admin.setLastLoginTime(null);
        if (baseMapper.updateByUserName(admin) < 1) {
            if (baseMapper.insert(admin) < 1) {
                LOGGER.error("addAdmin failed by unknown error, role:{}", admin);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }

        // 给管理员绑定角色
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq(DbConstants.Admin.USERNAME, admin.getUsername());
        Admin dbAdmin = baseMapper.selectOne(wrapper);
        if (Objects.isNull(dbAdmin)) {
            LOGGER.error("addAdmin failed cannot find admin by username:{}", admin.getUsername());
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        List<RoleDto> roleDtos = adminDto.getRoles();
        if (!CollectionUtils.isEmpty(roleDtos)) {
            for (RoleDto roleDto : roleDtos) {
                RoleAdmin roleAdmin = new RoleAdmin();
                roleAdmin.setAdminId(dbAdmin.getId());
                roleAdmin.setRoleId(roleDto.getId());
                if (roleAdminMapper.insert(roleAdmin) < 1) {
                    Role role = roleMapper.selectById(roleDto.getId());
                    LOGGER.error("addAdmin success, but add role failed by unknown error, username:{}, role:{}", dbAdmin.getUsername(), role);
                }
            }
        }
        return Response.ok();
    }

    /**
     * 删除管理员
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response delAdmin(Set<String> ids) throws Exception {
        for (String id : ids) {
            Admin admin = baseMapper.selectById(id);
            // 清理缓存信息
            redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + admin.getUsername());
            baseMapper.deleteById(id);
            // 删除角色信息
            UpdateWrapper<RoleAdmin> roleAdminUpdateWrapper = new UpdateWrapper<>();
            roleAdminUpdateWrapper.eq(DbConstants.RoleAdmin.ADMIN_ID, id);
            roleAdminMapper.delete(roleAdminUpdateWrapper);
        }
        return Response.ok();
    }

    /**
     * 从个人中心页面更改管理员信息
     *
     * @param adminDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response editAdminFromCenter(AdminDto adminDto) throws Exception {
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

    /**
     * 从个人中心页面修改邮箱
     *
     * @param passAndEmailDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateEmail(PassAndEmailDto passAndEmailDto) throws Exception {
        // 获取当前登陆用户信息
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        String currentUsername = SecurityUtils.getCurrentUsername();
        wrapper.eq(DbConstants.Admin.USERNAME, currentUsername);
        Admin admin = baseMapper.selectOne(wrapper);
        if (Objects.isNull(admin)) {
            LOGGER.error("updatePass failed, cannot find admin from db, username:{}", currentUsername);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 对密码进行校验
        String pass = RsaUtils.decryptByPrivateKey(mySecurityProperties.getRsaPrivateKey(), passAndEmailDto.getPass());
        if (!passwordEncoder.matches(pass, admin.getPassword())) {
            LOGGER.error("updateEmail failed, password is error");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED).message("修改失败，密码错误");
        }
        // 对验证码进行校验
        String redisCode = redisUtil.get(RedisConstants.EMAIL_CODE + RedisConstants.DIVISION + passAndEmailDto.getEmail());
        if (StringUtils.isBlank(redisCode) || !Objects.equals(redisCode, passAndEmailDto.getCode())) {
            LOGGER.error("updateEmail:{} failed, code is error, redisCode:[{}], code:[{}]", passAndEmailDto.getEmail(), redisCode, passAndEmailDto.getCode());
            return Response.error().message("修改失败，验证码错误");
        }
        // 更新用户邮箱
        admin.setEmail(passAndEmailDto.getEmail());
        baseMapper.updateById(admin);
        return Response.ok();
    }

    /**
     * 从个人中心页面修改密码
     * @param passAndEmailDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updatePass(PassAndEmailDto passAndEmailDto) throws Exception {
        // 获取当前登陆用户信息
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        String currentUsername = SecurityUtils.getCurrentUsername();
        wrapper.eq(DbConstants.Admin.USERNAME, currentUsername);
        Admin admin = baseMapper.selectOne(wrapper);
        if (Objects.isNull(admin)) {
            LOGGER.error("updatePass failed, cannot find admin from db, username:{}", currentUsername);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 对密码进行校验
        String oldPass = RsaUtils.decryptByPrivateKey(mySecurityProperties.getRsaPrivateKey(), passAndEmailDto.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(mySecurityProperties.getRsaPrivateKey(), passAndEmailDto.getNewPass());
        if (!passwordEncoder.matches(oldPass, admin.getPassword())) {
            LOGGER.error("updatePass failed, password is error");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED).message("修改失败，密码错误");
        }
        if (passwordEncoder.matches(newPass, admin.getPassword())) {
            LOGGER.error("updatePass failed, newPass cannot equal oldPass");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED).message("修改失败，旧密码不能与新密码相同");
        }
        admin.setPassword(passwordEncoder.encode(newPass));
        if (baseMapper.updateById(admin) < 1) {
            LOGGER.error("updatePass failed by unknown error, admin:{}", admin);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 从redis删除用户token
        redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + admin.getUsername());
        return Response.ok();
    }

    /**
     * 修改管理员
     * @param adminDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response editAdmin(AdminDto adminDto) throws Exception{
        QueryWrapper<Admin> usernameQueryWrapper = new QueryWrapper<>();
        usernameQueryWrapper.eq(DbConstants.Admin.USERNAME, adminDto.getUsername())
                .or()
                .eq(DbConstants.Admin.PHONE, adminDto.getPhone())
                .or()
                .eq(DbConstants.Admin.EMAIL, adminDto.getEmail());
        usernameQueryWrapper.eq(DbConstants.Base.IS_DELETED, 0);
        Admin dbAdmin = baseMapper.selectOne(usernameQueryWrapper);
        if (Objects.nonNull(dbAdmin) && !Objects.equals(dbAdmin.getId(), adminDto.getId())) {
            LOGGER.error("editAdmin failed, username or phone or email already exist, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED).message("保存失败，用户名、手机和邮箱不能重复");
        }
        Admin oldAdmin = baseMapper.selectById(adminDto.getId());
        Admin admin = toDb(adminDto, Admin.class);
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(DbConstants.Base.ID, admin.getId());
        if (baseMapper.update(admin, updateWrapper) < 1) {
            LOGGER.error("editAdmin failed by unknown error, admin:{}", admin);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 更改角色信息
        UpdateWrapper<RoleAdmin> roleAdminUpdateWrapper = new UpdateWrapper<>();
        roleAdminUpdateWrapper.eq(DbConstants.RoleAdmin.ADMIN_ID, adminDto.getId());
        roleAdminMapper.delete(roleAdminUpdateWrapper);
        List<RoleDto> roleDtos = adminDto.getRoles();
        if (!CollectionUtils.isEmpty(roleDtos)) {
            for (RoleDto roleDto : roleDtos) {
                RoleAdmin roleAdmin = new RoleAdmin();
                roleAdmin.setAdminId(dbAdmin.getId());
                roleAdmin.setRoleId(roleDto.getId());
                if (roleAdminMapper.insert(roleAdmin) < 1) {
                    Role role = roleMapper.selectById(roleDto.getId());
                    LOGGER.error("addAdmin success, but add role failed by unknown error, username:{}, role:{}", dbAdmin.getUsername(), role);
                }
            }
        }
        redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + oldAdmin.getUsername());
        return Response.ok();
    }

    @Override
    public void setDtoExtraProperties(Admin admin, AdminDto adminDto) {
        adminDto.setId(admin.getId());
        adminDto.setCreateTime(admin.getCreateTime());
        adminDto.setUpdateTime(admin.getUpdateTime());
        adminDto.setPassword(null);
        if (admin.getEnabled()) {
            adminDto.setEnabled(1);
        } else {
            adminDto.setEnabled(0);
        }
        // 查询角色信息
        List<RoleDto> roleDtos = toRoleDto(roleMapper.getRolesByUserId(admin.getId()));
        adminDto.setRoles(roleDtos);
    }

    @Override
    public void setDbExtraProperties(Admin admin, AdminDto adminDto) {
        if (StringUtils.isNotBlank(adminDto.getId())) {
            admin.setId(adminDto.getId());
        }
        if (Objects.nonNull(adminDto.getEnabled())) {
            if (Objects.equals(adminDto.getEnabled(), 1)) {
                admin.setEnabled(true);
            } else {
                admin.setEnabled(false);
            }
        }
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
