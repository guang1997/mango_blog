package com.myblog.service.security.config.handler;

import com.myblog.service.base.common.Constants;
import com.myblog.service.security.config.util.JSONAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出Handler
 */
@Component
public class MyLogoutHandler implements LogoutHandler {

    @Autowired
    private JSONAuthentication jsonAuthentication;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String headerToken = request.getHeader(Constants.ReplyField.HEADER);

        if (!StringUtils.isEmpty(headerToken)) {
            SecurityContextHolder.clearContext();
        }
    }
}
