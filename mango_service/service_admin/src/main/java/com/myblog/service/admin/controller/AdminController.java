package com.myblog.service.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.entity.MySecurityProperties;
import com.myblog.service.security.config.util.RsaUtils;
import com.myblog.service.security.config.util.SecurityUtils;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/manager")
@Api(value = "管理员相关接口", tags = {"管理员相关接口"})
public class AdminController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private MySecurityProperties mySecurityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RoleService roleService;

    @LogByMethod("/admin/manager/getAdminByPage")
    @ApiOperation(value = "分页查询管理员信息", notes = "分页查询管理员信息", response = Response.class)
    @PostMapping("/getAdminByPage")
    public Response getAdminByPage(@RequestBody AdminDto adminDto) throws Exception {
        return adminService.getAdminByPage(adminDto);
    }

    @LogByMethod("/admin/manager/addAdmin")
    @ApiOperation(value = "新增管理员", notes = "新增管理员", response = Response.class)
    @PostMapping("/addAdmin")
    public Response addAdmin(@RequestBody AdminDto adminDto) throws Exception {
        return adminService.addAdmin(adminDto);
    }

    @LogByMethod("/admin/manager/editAdmin")
    @ApiOperation(value = "修改管理员", notes = "修改管理员", response = Response.class)
    @PutMapping("/editAdmin")
    public Response editAdmin(@RequestBody AdminDto adminDto) {
        roleService.validRoleLevelByUserId(adminDto.getId());
        Response response = Response.ok();
        try {
//            response = roleService.editRole(roleDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/manager/editAdminFromCenter")
    @ApiOperation(value = "从个人中心页面修改管理员", notes = "从个人中心页面修改管理员", response = Response.class)
    @PutMapping("/editAdminFromCenter")
    public Response editAdminFromCenter(@RequestBody AdminDto adminDto) throws Exception {
        if (StringUtils.isBlank(adminDto.getQqNumber())
                || StringUtils.isBlank(adminDto.getWeChat())) {
            LOGGER.error("editAdminFromCenter failed, qqNumber or weChat cannot be null, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return adminService.editAdminFromCenter(adminDto);
    }

    @LogByMethod("/admin/manager/updatePass")
    @ApiOperation(value = "从个人中心页面修改密码", notes = "从个人中心页面修改密码", response = Response.class)
    @PostMapping("/updatePass")
    public Response updatePass(@RequestBody PassAndEmailDto passAndEmailDto) throws Exception {
        if (StringUtils.isBlank(passAndEmailDto.getOldPass())
                || StringUtils.isBlank(passAndEmailDto.getNewPass())) {
            LOGGER.error("updatePass failed, pass cannot be null");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }

        // 获取当前登陆用户信息
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        String currentUsername = SecurityUtils.getCurrentUsername();
        wrapper.eq(DbConstants.Admin.USERNAME, currentUsername);
        Admin admin = adminService.getOne(wrapper);
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
        if (!adminService.updateById(admin)) {
            LOGGER.error("updatePass failed by unknown error, admin:{}", admin);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 从redis删除用户token
        redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + admin.getUsername());
        return Response.ok();
    }

    @LogByMethod("/admin/manager/updateEmail")
    @ApiOperation(value = "从个人中心页面修改邮箱", notes = "从个人中心页面修改邮箱", response = Response.class)
    @PostMapping("/updateEmail")
    public Response updateEmail(@RequestBody PassAndEmailDto passAndEmailDto) throws Exception {
        if (StringUtils.isBlank(passAndEmailDto.getPass())
                || StringUtils.isBlank(passAndEmailDto.getEmail())) {
            LOGGER.error("updateEmail failed, pass or email cannot be null");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        // 获取当前登陆用户信息
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        String currentUsername = SecurityUtils.getCurrentUsername();
        wrapper.eq(DbConstants.Admin.USERNAME, currentUsername);
        Admin admin = adminService.getOne(wrapper);
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
        adminService.updateById(admin);
        return Response.ok();
    }

    @LogByMethod("/admin/manager/delAdmin")
    @ApiOperation(value = "删除管理员", notes = "删除管理员", response = Response.class)
    @DeleteMapping("/delAdmin")
    public Response delAdmin(@RequestBody Set<String> ids) throws Exception {
        for (String id : ids) {
            roleService.validRoleLevelByUserId(id);
        }
        return adminService.delAdmin(ids);
    }

}

