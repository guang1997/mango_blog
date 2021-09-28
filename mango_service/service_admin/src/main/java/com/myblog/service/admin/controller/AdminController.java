package com.myblog.service.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.admin.entity.Admin;
import com.myblog.service.admin.entity.Role;
import com.myblog.service.admin.entity.vo.LoginVo;
import com.myblog.service.admin.service.AdminService;
import com.myblog.service.admin.service.RoleService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.CheckUtils;
import com.myblog.service.base.util.JwtHelper;
import com.myblog.service.base.util.JwtInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@RequestMapping("/admin/auth")
public class AdminController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/doLogin")
    public Response doLogin(HttpServletRequest request, @RequestBody LoginVo loginVo) {
        if (loginVo == null || StringUtils.isBlank(loginVo.getUsername()) || StringUtils.isBlank(loginVo.getPassword())) {
            LOGGER.error("admin login failed, loginVo:{}", loginVo);
            return Response.error().code(ResultCodeEnum.LOGIN_ERROR.getCode()).message(ResultCodeEnum.LOGIN_ERROR.getMessage());
        }
        Response response = Response.ok();
        try {
            // 获取用户登录ip
//            String ipAddress = IpUtils.getIpAddr(request);
            // 校验数据
            String username = loginVo.getUsername();
            String password = loginVo.getPassword();
            boolean isEmail = CheckUtils.checkEmails(username);
            boolean isMobile = CheckUtils.checkMobile(username);
            // 根据用户名查询是否存在该用户
            QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
            if (isEmail) {
                adminWrapper.eq(DbConstants.Admin.email, username);
            } else if (isMobile) {
                adminWrapper.eq(DbConstants.Admin.mobile, username);
            } else {
                adminWrapper.eq(DbConstants.Admin.userName, username);
            }
            adminWrapper.eq(DbConstants.Base.isDeleted, "0");
            adminWrapper.eq(DbConstants.Admin.status, Constants.Status.ACTIVATED);
            Admin admin = adminService.getOne(adminWrapper);
            if (admin == null) {
                LOGGER.error("admin login failed, cannot find admin by userName:{}", username);
                return Response.error().code(ResultCodeEnum.LOGIN_ERROR_LOCKED.getCode()).message(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage());
            }
            // 对密码进行动态加盐处理
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matchPassword = passwordEncoder.matches(password, admin.getPassWord());
            if (!matchPassword) {
                LOGGER.error("admin login failed, password is error");
                return Response.error().code(ResultCodeEnum.LOGIN_ERROR_LOCKED.getCode()).message(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage());
            }
            // 获取用户权限
            List<String> roleIdList = new ArrayList<>();
            roleIdList.add(admin.getRoleId());
            List<Role> roles = roleService.listByIds(roleIdList);
            if (roles == null || roles.size() <= 0) {
                return Response.error().code(ResultCodeEnum.LOGIN_ERROR_ROLE.getCode()).message(ResultCodeEnum.LOGIN_ERROR_ROLE.getMessage());
            }
            // 生成token
            StringBuilder roleNames = new StringBuilder();
            roles.forEach(role -> roleNames.append(String.join(Constants.Symbol.COMMA,role.getRoleName())));
            String token = JwtHelper.createToken(new JwtInfo(admin.getId(), admin.getUserName(), admin.getAvatar(), roleNames.toString()));
            response.data(Constants.ResponseField.TOKEN,token);

        } catch (Exception e) {
            LOGGER.error("user:{} doLogin Exception:", loginVo.getUsername(), e);
            response = new Response();
            response.setMessage(ResultCodeEnum.LOGIN_ERROR.getMessage());
            response.setCode(ResultCodeEnum.LOGIN_ERROR.getCode());
        }
        return response;
    }
}

