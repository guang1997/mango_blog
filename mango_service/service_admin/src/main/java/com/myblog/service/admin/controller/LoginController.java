package com.myblog.service.admin.controller;

import com.myblog.service.security.entity.vo.MenuVo;
import com.myblog.service.security.entity.vo.Meta;
import com.myblog.service.security.util.TreeUtil;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.config.entity.Audience;
import com.myblog.service.security.config.entity.AuthUser;
import com.myblog.service.security.config.util.JwtTokenUtil;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.MenuService;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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
    private MenuService menuService;

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
     *
     * @return
     */
    @ApiOperation(value = "退出登录", notes = "退出登录", response = Response.class)
    @PostMapping(value = "/logout")
    public Response logout(HttpServletRequest request) {
        Response response = Response.ok()
                .code(ResultCodeEnum.LOGOUT_SUCCESS.getCode())
                .message(ResultCodeEnum.LOGOUT_SUCCESS.getMessage());
        try {
            String token = request.getHeader(Constants.ReplyField.HEADER);
            AuthUser authUser = jwtTokenUtil.getAuthUser(token, audience.getBase64Secret());
            if (authUser == null) {
                LOGGER.error("logOut Error, authUser:{}", authUser);
                return response.code(ResultCodeEnum.LOGOUT_ERROR.getCode()).message(ResultCodeEnum.LOGOUT_ERROR.getMessage());
            }
        } catch (Exception e) {
            response.code(ResultCodeEnum.LOGOUT_ERROR.getCode()).message(ResultCodeEnum.LOGOUT_ERROR.getMessage());
            LOGGER.error("logout Exception:", e);
        }
        return response;
    }

    /**
     * 获取用户菜单
     * 返回数据格式如下
     * [
     * {
     * hidden: true,
     * redirect: 'noRedirect',
     * alwaysShow: true,
     * name: 'router-name',
     * meta: {
     * roles: ['admin', 'editor']
     * title: 'title'
     * icon: 'svg-name'
     * noCache: true
     * breadcrumb: false
     * affix: true
     * }
     * }
     * ]
     *
     * @return
     */
    @ApiOperation(value = "获取当前用户菜单", notes = "获取当前用户菜单", response = Response.class)
    @GetMapping(value = "/getMenu")
    public Response getMenu(HttpServletRequest request) {
        Response response = Response.ok();
        try {
            String token = request.getHeader(Constants.ReplyField.HEADER);
            AuthUser authUser = jwtTokenUtil.getAuthUser(token, audience.getBase64Secret());
            if (authUser == null || authUser.getId() == null) {
                LOGGER.error("getMenu Error, authUser:{}", authUser);
                return response.code(ResultCodeEnum.GET_USERMENU_ERROR.getCode()).message(ResultCodeEnum.GET_USERMENU_ERROR.getMessage());
            }
            Admin admin = adminService.getById(authUser.getId());
            if (admin == null) {
                LOGGER.error("getMenu Error, admin:{}", admin);
                return response.code(ResultCodeEnum.GET_USERMENU_ERROR.getCode()).message(ResultCodeEnum.GET_USERMENU_ERROR.getMessage());
            }
            // 获取用户角色信息
            List<Role> roles = roleService.getRolesByUserName(admin.getUserName());
            List<String> roleNames = roles.stream().map(Role::getRoleName).collect(Collectors.toList());
            // 根据角色信息获取用户菜单列表
            List<Menu> menus = menuService.getMenuByRoles(roles);
            if (menus == null) {
                LOGGER.error("getMenu Error, admin:{}, menus:{}", authUser, menus);
                return response.code(ResultCodeEnum.GET_USERMENU_ERROR.getCode()).message(ResultCodeEnum.GET_USERMENU_ERROR.getMessage());
            }
            // 组装菜单信息给页面
            List<MenuVo> menuVos = new ArrayList<>();
            for (Menu menu : menus) {
                menuVos.add(convertToMenuVo(menu, roleNames));
            }
            List<MenuVo> resultList = TreeUtil.toTree(menuVos, "1");
            response.data(Constants.ReplyField.MENU, resultList);
        } catch (Exception e) {
            response.code(ResultCodeEnum.LOGOUT_ERROR.getCode()).message(ResultCodeEnum.LOGOUT_ERROR.getMessage());
            LOGGER.error("logout Exception:", e);
        }
        return response;
    }

    private MenuVo convertToMenuVo(Menu menu, List<String> roleNames) {
        MenuVo menuVo = new MenuVo();
        menuVo.setId(menu.getId());
        menuVo.setPid(menu.getPid());
        menuVo.setHidden(menu.getHidden());
        menuVo.setName(menu.getName());
        menuVo.setChildren(new ArrayList<>());
        menuVo.setComponent(menu.getComponent());
        menuVo.setPath(menu.getPath());
        menuVo.setRedirect(menu.getRedirect());
        Meta meta = new Meta();
        meta.setTitle(menu.getTitle());
        meta.setIcon(menu.getIcon());
        meta.setRoles(roleNames);
        menuVo.setMeta(meta);
        return menuVo;
    }
}
