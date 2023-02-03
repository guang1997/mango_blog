package com.myblog.service.admin.controller;

import com.myblog.service.admin.entity.dto.UserInfoDto;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.annotation.rest.AnonymousPostMapping;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.entity.MySecurityProperties;
import com.myblog.service.security.entity.dto.LoginDto;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.entity.dto.Meta;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
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
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/auth")
@Api(value = "后台登录相关接口", tags = {"后台登录相关接口"})
public class LoginController {

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
    @LogByMethod(value = "/admin/auth/login", validate = true)
    @ApiOperation(value = "登录", notes = "登录", response = Response.class)
    @AnonymousPostMapping(value = "/login")
    public ResultModel<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request) throws Exception {
        String token = "";
        // 校验用户是否失败次数过多
        String key = String.join(RedisConstants.DIVISION, RedisConstants.LOGIN_LIMIT, loginDto.getUsername(), IpUtils.getIpAddr(request));
        String limitCount = redisUtil.get(key);
        if (StringUtils.isNotEmpty(limitCount)) {
            int tempLimitCount = Integer.parseInt(limitCount);
            if (tempLimitCount <= 1) {
                return ResultModel.setResult(ResultCodeEnum.LOGIN_LOCKED);
            }
        }

        Admin admin = adminService.checkLogin(loginDto);
        if (admin == null) {
            return ResultModel.<String>setResult(ResultCodeEnum.LOGIN_ERROR_LOCKED).message(String.format(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage(),
                    setLoginCommit(key)));
        }
        // 如果账号已禁用，登陆失败
        if (!admin.getEnabled()) {
            return ResultModel.<String>setResult(ResultCodeEnum.LOGIN_ERROR).message("登陆失败，该账号已被禁用，请联系管理员处理");
        }
        //查找角色
        List<Role> roles =  roleService.getRolesByUserId(admin.getId());

        // 创建token
        long rememberMe = (loginDto.getRememberMe() == null || !loginDto.getRememberMe())
                ? properties.getExpiresSecond() : properties.getRememberMeExpiresSecond();
        // 从缓存中获取token
        token = redisUtil.get(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + admin.getUsername());
        if (StringUtils.isBlank(token)) {
            token = jwtTokenUtil.createToken(admin, roles, rememberMe, properties.getBase64Secret());
            //把新的token存储到缓存中
            redisUtil.setEx(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + loginDto.getUsername(), token, rememberMe / ( 1000 * 60 * 60 ), TimeUnit.HOURS);
            log.info("cannot find token from redis cache, create it:{}", token);
        }
        // 登录成功之后更新数据库信息
        admin.setLastLoginIp(IpUtils.getIpAddr(request));
        admin.setLastLoginTime(new Date());
        admin.setLoginCount(admin.getLoginCount() + 1);
        adminService.updateById(admin);
        // 将token返回到页面
        return ResultModel.ok(token);
    }
    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @LogByMethod("/admin/auth/info")
    @ApiOperation(value = "用户信息", notes = "用户信息", response = Response.class)
    @GetMapping("/info")
    public ResultModel<UserInfoDto> info(HttpServletRequest request) throws Exception {

        String token = request.getHeader(Constants.ReplyField.HEADER);
        AuthUser authUser = jwtTokenUtil.getAuthUser(token, properties.getBase64Secret());
        if (Objects.isNull(authUser)) {
            log.error("getAdminInfo Error, authUser:{}", authUser);
            return ResultModel.setResult(ResultCodeEnum.GET_USERINFO_ERROR);
        }
        Admin admin = adminService.getById(authUser.getId());
        if (Objects.isNull(admin)) {
            log.error("getAdminInfo Error, admin is null, authUser:{}", authUser);
            return ResultModel.setResult(ResultCodeEnum.GET_USERINFO_ERROR);
        }
        // 获取角色信息
        List<Role> roles = roleService.getRolesByUserId(admin.getId());
        // 获取角色对应的按钮信息
        List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<String> menuButtons = roleService.getMenusByRoleId(roleIds);
        if (!CollectionUtils.isEmpty(menuButtons)) {
            menuButtons = menuButtons.stream().distinct().collect(Collectors.toList());
        }
        Set<String> roleNameSets = roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setRoles(roleNameSets);
        userInfoDto.setToken(token);
        userInfoDto.setUsername(admin.getUsername());
        userInfoDto.setNickname(admin.getNickname());
        userInfoDto.setPhone(admin.getPhone());
        userInfoDto.setEmail(admin.getEmail());
        userInfoDto.setGender(admin.getGender());
        userInfoDto.setQqNumber(admin.getQqNumber());
        userInfoDto.setWeChat(admin.getWeChat());
        userInfoDto.setId(authUser.getId());
        userInfoDto.setAvatar(admin.getAvatar());
        userInfoDto.setMenuButtons(menuButtons);

        return ResultModel.ok(userInfoDto);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @LogByMethod("/admin/auth/logout")
    @ApiOperation(value = "退出登录", notes = "退出登录", response = Response.class)
    @AnonymousPostMapping(value = "/logout")
    public ResultModel<Object> logout(HttpServletRequest request) throws Exception {
        String token = request.getHeader(Constants.ReplyField.HEADER);
        AuthUser authUser = jwtTokenUtil.getAuthUser(token, properties.getBase64Secret());
        if (authUser == null) {
            log.error("logOut Error, authUser:{}", authUser);
            return ResultModel.setResult(ResultCodeEnum.LOGOUT_ERROR);
        }
        // 去掉redis中token信息
        redisUtil.delete(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + authUser.getUsername());
        return ResultModel.ok();
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
    @LogByMethod("/admin/auth/getMenu")
    @ApiOperation(value = "获取当前用户菜单", notes = "获取当前用户菜单", response = Response.class)
    @GetMapping(value = "/getMenu")
    public ResultModel<List<MenuDto>> getMenu(HttpServletRequest request) throws Exception {
        String token = request.getHeader(Constants.ReplyField.HEADER);
        AuthUser authUser = jwtTokenUtil.getAuthUser(token, properties.getBase64Secret());
        if (authUser == null || authUser.getId() == null) {
            log.error("getMenu Error, authUser:{}", authUser);
            return ResultModel.setResult(ResultCodeEnum.GET_USERMENU_ERROR);
        }
        Admin admin = adminService.getById(authUser.getId());
        if (admin == null) {
            log.error("getMenu Error, admin:{}", admin);
            return ResultModel.setResult(ResultCodeEnum.GET_USERMENU_ERROR);
        }
        // 获取用户角色信息
        List<Role> roles = roleService.getRolesByUserId(admin.getId());
        List<String> roleNames = roles.stream().map(Role::getRoleName).collect(Collectors.toList());
        // 根据角色信息获取用户菜单列表
        List<Menu> menus = menuService.getMenuByRoles(roles);
        if (menus == null) {
            log.error("getMenu Error, admin:{}, menus:{}", authUser, menus);
            return ResultModel.setResult(ResultCodeEnum.GET_USERMENU_ERROR);
        }
        // 组装菜单信息给页面
        List<MenuDto> menuDtos = new ArrayList<>();
        for (Menu menu : menus) {
            menuDtos.add(convertToMenuDto(menu, roleNames));
        }
        List<MenuDto> resultList = TreeUtil.toMenuDtoTree(menuDtos, "0");
        return ResultModel.ok(resultList);
    }

    private MenuDto convertToMenuDto(Menu menu, List<String> roleNames) {
        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(menu, menuDto);
        menuDto.setChildren(new ArrayList<>());
        menuDto.setId(menu.getId());
        menuDto.setCreateTime(menu.getCreateTime());
        Meta meta = new Meta(roleNames, menu.getTitle(), menu.getIcon());
        menuDto.setMeta(meta);
        return menuDto;
    }

    /**
     * 设置登录次数
     *
     * @param key
     * @return
     */
    private Integer setLoginCommit(String key) {
        String loginCountStr = redisUtil.get(key);
        Integer limitCount = this.loginLimitCount;
        if (StringUtils.isBlank(loginCountStr)) {
            // 第一次登陆失败时放到缓存中
            redisUtil.setEx(key, String.valueOf(--limitCount), 30, TimeUnit.MINUTES);
            return limitCount;
        }
        // 之后每登陆错误一次，次数就减1
        int loginCount = Integer.parseInt(loginCountStr);
        redisUtil.setEx(key, String.valueOf(--loginCount), 30, TimeUnit.MINUTES);
        return loginCount;
    }
}
