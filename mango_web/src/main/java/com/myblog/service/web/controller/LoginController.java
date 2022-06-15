package com.myblog.service.web.controller;

import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.service.VerificationCodeService;
import com.myblog.service.web.entity.dto.UserDto;
import com.myblog.service.web.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 登陆 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-28
 */
@CrossOrigin
@RestController
@RequestMapping("/web/login")
public class LoginController {

    private static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserService userService;

    @LogByMethod(value = "/web/login/sendCode")
    @ApiOperation(value = "发送验证码", notes = "发送验证码", response = Response.class)
    @PostMapping("/sendCode")
    public Response sendCode(@RequestBody UserDto userDto) {
        if (StringUtils.isBlank(userDto.getEmail())) {
            LOGGER.error("sendCode failed, email:[{}] cannot be null", userDto.getEmail());
            return Response.error().message("邮箱不能为空");
        }
        return verificationCodeService.sendCode(userDto.getEmail(), Constants.EmailSource.WEB);
    }

    @LogByMethod(value = "/web/login/doLogin", behavior = BehaviorEnum.LOGIN)
    @ApiOperation(value = "保存用户", notes = "保存用户", response = Response.class)
    @PostMapping("/doLogin")
    public Response doLogin(@RequestBody UserDto userDto) throws Exception {
        // 校验验证码
        if (!verificationCodeService.validateCode(userDto.getEmail(), userDto.getCode(), Constants.EmailSource.WEB).getSuccess()) {
            return Response.error().message("验证码错误");
        }
        return userService.doLogin(userDto);
    }
}
