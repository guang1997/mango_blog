package com.myblog.service.admin.controller;

import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.entity.Audience;
import com.myblog.service.security.config.entity.AuthUser;
import com.myblog.service.security.config.util.JwtTokenUtil;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 登录
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-04
 */
@RestController
@RequestMapping("/admin/auth")
@Api(value = "后台登录相关接口", tags = {"后台登录相关接口"})
public class LoginController {

    private static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private Audience audience;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "用户信息", notes = "用户信息", response = Response.class)
    @GetMapping("/info")
    public Response info(HttpServletRequest request) {

        Response response = Response.ok();
        try {
            String token = request.getHeader(Constants.ReplyField.HEADER);
            AuthUser authUser = jwtTokenUtil.getAuthUser(token, audience.getBase64Secret());
            if (authUser == null) {
                LOGGER.error("getAdminInfo Error, authUser:{}", authUser);
                return response.code(ResultCodeEnum.GET_USERINFO_ERROR.getCode()).message(ResultCodeEnum.GET_USERINFO_ERROR.getMessage());
            }
            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }
//            Admin admin = adminService.getById(authUser.getId());
//            String roleId = admin;
//            List<String> roleIds = new ArrayList<>();
//            roleIds.add(roleId);
//            List<Role> roles = roleService.listByIds(roleIds);

            response.data(Constants.ReplyField.ROLES, roles)
                    .data(Constants.ReplyField.TOKEN, token)
                    .data(Constants.ReplyField.USER_NAME, authUser.getUsername())
                    .data(Constants.ReplyField.AVATAR, authUser.getAvatar());
        } catch (Exception e) {
            response.code(ResultCodeEnum.GET_USERINFO_ERROR.getCode()).message(ResultCodeEnum.GET_USERINFO_ERROR.getMessage());
            LOGGER.error("getUserInfo Exception:", e);
        }
        return response;
    }

    /**
     * 退出登录
     * @return
     */
//    @ApiOperation(value = "退出登录", notes = "退出登录", response = Response.class)
//    @PostMapping(value = "/logout")
//    public Response logout(HttpServletRequest request) {
//        Response response = Response.ok();
//        try {
//            String token = request.getHeader(Constants.ReplyField.HEADER);
//            AuthUser authUser = jwtTokenUtil.getAuthUser(token, audience.getBase64Secret());
//            if (authUser == null) {
//                LOGGER.error("logOut Error, authUser:{}", authUser);
//                return response.code(ResultCodeEnum.LOGOUT_ERROR.getCode()).message(ResultCodeEnum.LOGOUT_ERROR.getMessage());
//            }
//            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
//            List<String> roles = new ArrayList<>();
//            for (GrantedAuthority authority : authorities) {
//                roles.add(authority.getAuthority());
//            }
////            Admin admin = adminService.getById(authUser.getId());
////            String roleId = admin;
////            List<String> roleIds = new ArrayList<>();
////            roleIds.add(roleId);
////            List<Role> roles = roleService.listByIds(roleIds);
//
//            response.data(Constants.ReplyField.ROLES, roles)
//                    .data(Constants.ReplyField.TOKEN, token)
//                    .data(Constants.ReplyField.USER_NAME, authUser.getUsername())
//                    .data(Constants.ReplyField.AVATAR, authUser.getAvatar());
//        } catch (Exception e) {
//            response.code(ResultCodeEnum.GET_USERINFO_ERROR.getCode()).message(ResultCodeEnum.GET_USERINFO_ERROR.getMessage());
//            LOGGER.error("getUserInfo Exception:", e);
//        }
//        return response;
//    }
}
