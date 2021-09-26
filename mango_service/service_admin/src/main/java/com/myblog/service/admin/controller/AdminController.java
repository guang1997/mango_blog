package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.vo.LoginVo;
import com.myblog.service.admin.service.AdminService;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.JwtHelper;
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
@CrossOrigin
@RestController
@RequestMapping("/admin/auth")
public class AdminController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @PostMapping("/doLogin")
    public Response doLogin(@RequestBody LoginVo loginVo) {
        Response response;
        try {
            response = adminService.doLogin(loginVo);
        } catch (Exception e) {
            LOGGER.error("user:{} doLogin Exception:", loginVo.getUsername(), e);
            response = new Response();
            response.setMessage(ResultCodeEnum.LOGIN_ERROR.getMessage());
            response.setCode(ResultCodeEnum.LOGIN_ERROR.getCode());
        }
        return response;
    }
}

