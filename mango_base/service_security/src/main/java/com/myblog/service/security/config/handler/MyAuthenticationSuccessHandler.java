package com.myblog.service.security.config.handler;

import com.baomidou.mybatisplus.extension.api.R;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.entity.Audience;
import com.myblog.service.security.config.entity.AuthUser;
import com.myblog.service.security.config.util.JSONAuthentication;
import com.myblog.service.security.config.util.JwtTokenUtil;
import com.myblog.service.security.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功操作
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    @Autowired
    private JSONAuthentication jsonAuthentication;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private Audience audience;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Response reply = Response.ok();
        //取得账号信息
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String username = userDetails.getUsername();
            // 从缓存中获取token
            String token = redisUtil.get(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + username);
            if (token == null) {
                String secretKey = audience.getBase64Secret();

                //如果token为空，则去创建一个新的token
                token = jwtTokenUtil.createToken((AuthUser) userDetails, secretKey);
                //把新的token存储到缓存中
                redisUtil.setEx(RedisConstants.TOKEN_KEY + RedisConstants.DIVISION + userDetails.getUsername(), token, 24, TimeUnit.HOURS);
                LOGGER.info("cannot find token from redis cache, create it:{}", token);
            }

            //将token返回到页面
            reply.data(Constants.ReplyField.TOKEN, token);
            //输出
            jsonAuthentication.WriteJSON(request, response, reply);
        } catch (Exception e) {
            LOGGER.error("create admin token Exception:", e);
            reply.code(ResultCodeEnum.LOGIN_ERROR.getCode()).message(ResultCodeEnum.LOGIN_ERROR.getMessage());
            jsonAuthentication.WriteJSON(request, response, reply);
        }
    }
}
