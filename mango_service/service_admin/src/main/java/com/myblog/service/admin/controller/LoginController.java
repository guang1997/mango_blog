package com.myblog.service.admin.controller;

import com.myblog.service.base.annotation.rest.AnonymousPostMapping;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.entity.MySecurityProperties;
import com.myblog.service.security.config.entity.vo.AdminVO;
import com.myblog.service.security.entity.vo.MenuVo;
import com.myblog.service.security.entity.vo.Meta;
import com.myblog.service.security.util.TreeUtil;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 登录
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-04
 */
@CrossOrigin
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
    private MySecurityProperties properties;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${auth.loginLimitCount}")
    private Integer loginLimitCount;
    /**
     * 登录
     *
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录", response = Response.class)
    @AnonymousPostMapping(value = "/login")
    public Response login(@RequestBody AdminVO adminVO, HttpServletRequest request) {
        Response response = Response.ok();
        if (adminVO == null || StringUtils.isEmpty(adminVO.getUsername()) || StringUtils.isEmpty(adminVO.getPassword())) {
            LOGGER.error("admin:{} login error", adminVO);
            return Response.setResult(ResultCodeEnum.LOGIN_ERROR);
        }
        try {
            // 校验用户是否失败次数过多
            String ipAddress = IpUtils.getIpAddr(request);
            String limitCount = redisUtil.get(RedisConstants.LOGIN_LIMIT + RedisConstants.DIVISION + ipAddress);
            if (StringUtils.isNotEmpty(limitCount)) {
                int tempLimitCount = Integer.parseInt(limitCount);
                if (tempLimitCount >= loginLimitCount) {
                    return Response.setResult(ResultCodeEnum.LOGIN_LOCKED);
                }
            }

            Admin admin = adminService.checkLogin(adminVO);
            if (admin == null) {
                response.code(ResultCodeEnum.LOGIN_ERROR_LOCKED.getCode()).message(String.format(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage(), setLoginCommit(ipAddress)));
                return response;
            }
            //查找角色
            List<Role> roles =  roleService.getRolesByUserName(admin.getUsername());

            // 创建token
            long rememberMe = (adminVO.getRememberMe() == null || !adminVO.getRememberMe())
                    ? properties.getExpiresSecond() : properties.getRememberMeExpiresSecond();
            // 从缓存中获取token
            String token = redisUtil.get(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + admin.getUsername());
            if (StringUtils.isBlank(token)) {
                token = jwtTokenUtil.createToken(admin, roles, rememberMe, properties.getBase64Secret());
                //把新的token存储到缓存中
                redisUtil.setEx(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + adminVO.getUsername(), token, rememberMe / ( 1000 * 60 * 60 ), TimeUnit.HOURS);
                LOGGER.info("cannot find token from redis cache, create it:{}", token);
            }
            //将token返回到页面
            response.data(Constants.ReplyField.TOKEN, token);
        } catch (Exception e) {
            LOGGER.error("attemptAuthentication failed, Exception:", e);
            return Response.setResult(ResultCodeEnum.LOGIN_ERROR);
        }
        return response;
    }
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
            AuthUser authUser = jwtTokenUtil.getAuthUser(token, properties.getBase64Secret());
            if (authUser == null) {
                LOGGER.error("getAdminInfo Error, authUser:{}", authUser);
                return response.code(ResultCodeEnum.GET_USERINFO_ERROR.getCode()).message(ResultCodeEnum.GET_USERINFO_ERROR.getMessage());
            }
            Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                roles.add(authority.getAuthority());
            }

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
    @AnonymousPostMapping(value = "/logout")
    public Response logout(HttpServletRequest request) {
        Response response = Response.ok();
        try {
            String token = request.getHeader(Constants.ReplyField.HEADER);
            AuthUser authUser = jwtTokenUtil.getAuthUser(token, properties.getBase64Secret());
            if (authUser == null) {
                LOGGER.error("logOut Error, authUser:{}", authUser);
                return response.code(ResultCodeEnum.LOGOUT_ERROR.getCode()).message(ResultCodeEnum.LOGOUT_ERROR.getMessage());
            }
            // 去掉redis中token信息
            redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + authUser.getUsername());
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
            AuthUser authUser = jwtTokenUtil.getAuthUser(token, properties.getBase64Secret());
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
            List<Role> roles = roleService.getRolesByUserName(admin.getUsername());
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
            List<MenuVo> resultList = TreeUtil.toTree(menuVos, "0");
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
        menuVo.setCreateTime(menu.getCreateTime());
        Meta meta = new Meta(roleNames, menu.getTitle(), menu.getIcon());
        menuVo.setMeta(meta);
        return menuVo;
    }

    /**
     * 设置登录次数
     *
     * @param ipAddress
     * @return
     */
    private Integer setLoginCommit(String ipAddress) {
        String key = RedisConstants.LOGIN_LIMIT + RedisConstants.DIVISION + ipAddress;
        String loginCountStr = redisUtil.get(key);
        Integer limitCount = this.loginLimitCount;
        if (StringUtils.isEmpty(loginCountStr)) {
            redisUtil.setEx(key, String.valueOf(--limitCount), 30, TimeUnit.MINUTES);
        } else {
            int loginCount = Integer.parseInt(loginCountStr);
            limitCount -= ++loginCount;
            redisUtil.setEx(key, String.valueOf(limitCount), 30, TimeUnit.MINUTES);
        }
        return limitCount;
    }
}
