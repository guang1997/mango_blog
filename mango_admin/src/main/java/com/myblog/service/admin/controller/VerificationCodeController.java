package com.myblog.service.admin.controller;

import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 验证码
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-03
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/code")
@Api(value = "验证码相关接口", tags = {"验证码相关接口"})
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @LogByMethod(value = "/admin/code/sendCode")
    @ApiOperation(value = "发送验证码", notes = "发送验证码", response = Response.class)
    @GetMapping("/sendCode")
    public ResultModel<Object> sendCode(String email) {
        if (StringUtils.isBlank(email)) {
            log.error("sendCode failed, email:[{}] cannot be null", email);
            return ResultModel.error().message("邮箱不能为空");
        }
        if (verificationCodeService.sendCode(email, Constants.EmailSource.ADMIN)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SEND_CODE_FAILED);
    }
}
