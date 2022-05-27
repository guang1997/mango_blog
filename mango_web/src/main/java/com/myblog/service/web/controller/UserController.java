package com.myblog.service.web.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.service.VerificationCodeService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-27
 */
@RestController
@RequestMapping("/web/user")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    List<ServiceTest> serviceTests;

    @PostConstruct
    public void init() {
        System.out.println(serviceTests);
    }
    @LogByMethod(value = "/web/user/sendCode")
    @ApiOperation(value = "发送验证码", notes = "发送验证码", response = Response.class)
    @GetMapping("/sendCode")
    public Response sendCode(String email) {
        if (StringUtils.isBlank(email)) {
            LOGGER.error("sendCode failed, email:[{}] cannot be null", email);
            return Response.error().message("邮箱不能为空");
        }
        return verificationCodeService.sendCode(email, Constants.EmailSource.ADMIN);
    }
}

