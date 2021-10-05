package com.myblog.service.admin.controller;


import com.myblog.service.base.util.*;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/admin")
@Api(value = "管理员相关接口", tags = {"管理员相关接口"})
public class AdminController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisUtil redisUtil;



//    @PostMapping("/login")
//    public Response doLogin(HttpServletRequest request, @RequestBody LoginVo loginVo) {
//        if (loginVo == null || StringUtils.isBlank(loginVo.getUsername()) || StringUtils.isBlank(loginVo.getPassword())) {
//            LOGGER.error("admin login failed, loginVo:{}", loginVo);
//            return Response.error().code(ResultCodeEnum.LOGIN_ERROR.getCode()).message(ResultCodeEnum.LOGIN_ERROR.getMessage());
//        }
//        Response response = Response.ok();
//        try {
//
//            // 获取用户登录ip
//            String ipAddress = IpUtils.getIpAddr(request);
//            // 校验数据
//            String username = loginVo.getUsername().trim();
//            String password = loginVo.getPassword().trim();
//            boolean isEmail = CheckUtils.checkEmails(username);
//            boolean isMobile = CheckUtils.checkMobile(username);
//            // 根据用户名查询是否存在该用户
//            QueryWrapper<Admin> adminWrapper = new QueryWrapper<>();
//            if (isEmail) {
//                adminWrapper.eq(DbConstants.Admin.email, username);
//            } else if (isMobile) {
//                adminWrapper.eq(DbConstants.Admin.mobile, username);
//            } else {
//                adminWrapper.eq(DbConstants.Admin.userName, username);
//            }
//            adminWrapper.eq(DbConstants.Base.isDeleted, "0");
//            adminWrapper.eq(DbConstants.Admin.status, Constants.Status.ACTIVATED);
//            Admin admin = adminService.getOne(adminWrapper);
//            if (admin == null) {
//                LOGGER.error("admin login failed, cannot find admin by userName:{}", username);
//                return Response.error().code(ResultCodeEnum.LOGIN_ERROR_LOCKED.getCode()).message(String.format(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage(), setLoginCommit(ipAddress)));
//            }
//            // 对密码进行动态加盐处理
//            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            boolean matchPassword = passwordEncoder.matches(password, admin.getPassWord());
//            if (!matchPassword) {
//                LOGGER.error("admin login failed, password is error");
//                return Response.error().code(ResultCodeEnum.LOGIN_ERROR_LOCKED.getCode()).message(String.format(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage(), setLoginCommit(ipAddress)));
//            }
//            // 获取用户权限
//            List<String> roleIdList = new ArrayList<>();
//            roleIdList.add(admin.getRoleId());
//            List<Role> roles = roleService.listByIds(roleIdList);
//            if (roles == null || roles.size() <= 0) {
//                return Response.error().code(ResultCodeEnum.LOGIN_ERROR_ROLE.getCode()).message(ResultCodeEnum.LOGIN_ERROR_ROLE.getMessage());
//            }
//            // 生成token
//            StringBuilder roleNames = new StringBuilder();
//            roles.forEach(role -> roleNames.append(String.join(Constants.Symbol.COMMA,role.getRoleName())));
//            String token = JwtTokenUtil.createToken(new JwtInfo(admin.getId(), admin.getUserName(), admin.getAvatar(), roleNames.toString()));
//            response.data(Constants.ReplyField.TOKEN,token);
//
//            // 更新数据库中的数据
//            admin.setLoginCount(admin.getLoginCount() + 1);
//            admin.setLastLoginIp(ipAddress);
//            admin.setLastLoginTime(new Date());
//            adminService.save(admin);
//        } catch (Exception e) {
//            LOGGER.error("user:{} doLogin Exception:", loginVo.getUsername(), e);
//            response = new Response();
//            response.setMessage(ResultCodeEnum.LOGIN_ERROR.getMessage());
//            response.setCode(ResultCodeEnum.LOGIN_ERROR.getCode());
//        }
//        return response;
//    }



}

