package com.myblog.service.security.config.handler;


import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.security.config.util.JSONAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败操作
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationFailureHandler.class);

    @Autowired
    private JSONAuthentication jsonAuthentication;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        String ipAddress = IpUtils.getIpAddr(request);
        LOGGER.error("ip:{} login error, ", ipAddress);
        Response data = Response.error().code(ResultCodeEnum.LOGIN_ERROR.getCode()).message(ResultCodeEnum.LOGIN_ERROR.getMessage());
        jsonAuthentication.WriteJSON(request, response, data);
    }
}