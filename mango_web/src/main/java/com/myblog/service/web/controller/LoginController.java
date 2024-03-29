package com.myblog.service.web.controller;

import com.myblog.service.base.common.*;
import com.myblog.service.base.common.Constants.EmailSource;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.security.service.VerificationCodeService;
import com.myblog.service.web.entity.dto.UserDto;
import com.myblog.service.web.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 登陆 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-28
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/web/login")
public class LoginController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    @LogByMethod(value = "/web/login/sendCode")
    @ApiOperation(value = "发送验证码", notes = "发送验证码", response = Response.class)
    @PostMapping("/sendCode")
    public ResultModel<Object> sendCode(@RequestBody UserDto userDto) {
        if (StringUtils.isBlank(userDto.getEmail())) {
            log.error("sendCode failed, email:[{}] cannot be null", userDto.getEmail());
            return ResultModel.error().message("邮箱不能为空");
        }
        if (verificationCodeService.sendCode(userDto.getEmail(), EmailSource.WEB)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SEND_CODE_FAILED);
    }

    @LogByMethod(value = "/web/login/doLogin", behavior = BehaviorEnum.LOGIN)
    @ApiOperation(value = "保存用户", notes = "保存用户", response = Response.class)
    @PostMapping("/doLogin")
    public ResultModel<UserDto> doLogin(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception {
        // 校验验证码
        if (!verificationCodeService.validateCode(userDto.getEmail(), userDto.getCode(), Constants.EmailSource.WEB)) {
            return ResultModel.<UserDto>error().message("验证码错误");
        }
        return ResultModel.ok(userService.doLogin(userDto, request));
    }

    @LogByMethod(value = "/web/login/getUser", behavior = BehaviorEnum.LOGIN)
    @ApiOperation(value = "获取用户", notes = "获取用户", response = Response.class)
    @PostMapping("/getUser")
    public ResultModel<UserDto> getUser(@RequestBody UserDto userDto) throws Exception {
        return ResultModel.ok(userService.getUser(userDto));
    }
}
