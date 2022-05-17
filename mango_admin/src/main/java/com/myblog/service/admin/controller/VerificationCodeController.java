package com.myblog.service.admin.controller;

import com.myblog.service.admin.service.VerificationCodeService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 验证码
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-03
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/code")
@Api(value = "验证码相关接口", tags = {"验证码相关接口"})
public class VerificationCodeController {

    private static Logger LOGGER = LoggerFactory.getLogger(VerificationCodeController.class);

    @Autowired
    private VerificationCodeService verificationCodeService;

    @LogByMethod(value = "/admin/code/sendCode")
    @ApiOperation(value = "发送验证码", notes = "发送验证码", response = Response.class)
    @GetMapping("/sendCode")
    public Response sendCode(String email) {
        if (StringUtils.isBlank(email)) {
            LOGGER.error("sendCode failed, email:[{}] cannot be null", email);
            return Response.error().message("邮箱不能为空");
        }
        return verificationCodeService.sendCode(email);
    }
}
