package com.myblog.service.security.config.handler;

import com.baomidou.mybatisplus.extension.api.R;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.config.util.JSONAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 成功退出处理器
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

    @Autowired
    private JSONAuthentication jsonAuthentication;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String username = user.getUsername();
        Response reply = Response.ok().code(ResultCodeEnum.LOGOUT_SUCCESS.getCode()).message(ResultCodeEnum.LOGOUT_SUCCESS.getMessage());
        LOGGER.debug("username:{} logout success", username);
        jsonAuthentication.WriteJSON(request, response, reply);
    }
}
