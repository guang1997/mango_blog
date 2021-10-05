package com.myblog.service.security.config.filter;

import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.entity.Audience;
import com.myblog.service.security.config.util.JSONAuthentication;
import com.myblog.service.security.config.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器，验证token有效性
 */
@Component
public class MyOncePerRequestFilter extends OncePerRequestFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(MyOncePerRequestFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private Audience audience;

    @Autowired
    private JSONAuthentication jsonAuthentication;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String headerToken = request.getHeader(Constants.ReplyField.HEADER);

        if (!StringUtils.isEmpty(headerToken)) {
            //postMan测试时，自动加入的前缀，要去掉。
            String token = headerToken.replace("Bearer", "").trim();
            LOGGER.info("get Token:{} from server", token);
            String secretKey = audience.getBase64Secret();
            try {
                boolean check = this.jwtTokenUtil.isExpiration(token, secretKey);
                // token已过期
                if (check) {
                    LOGGER.error("token:{} has expired",token);
                    Response errorResp = Response.error();
                    errorResp.code(ResultCodeEnum.LOGIN_ERROR_BY_TOKEN_EXPIRED.getCode()).message(ResultCodeEnum.LOGIN_ERROR_BY_TOKEN_EXPIRED.getMessage());
                    jsonAuthentication.WriteJSON(request, response, errorResp);
                    chain.doFilter(request, response);
                    return;
                }
                //通过令牌获取用户名称
                String username = jwtTokenUtil.getUsername(token, secretKey);
                LOGGER.info("get userName:{} from token", username);
                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //通过用户信息得到UserDetails
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //验证令牌有效性
                    boolean validata = jwtTokenUtil.validateToken(token, userDetails, secretKey);
                    // 令牌无效
                    if (!validata) {
                        LOGGER.error("token:{} validata:{}",token, validata);
                        Response errorResp = Response.error();
                        errorResp.code(ResultCodeEnum.LOGIN_ERROR_BY_VALIDATA_TOKEN.getCode()).message(ResultCodeEnum.LOGIN_ERROR_BY_VALIDATA_TOKEN.getMessage());
                        jsonAuthentication.WriteJSON(request, response, errorResp);
                        chain.doFilter(request, response);
                        return;
                    }
                    // 将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                LOGGER.error("check Token Exception:", e);
                Response errorResp = Response.error();
                errorResp.code(ResultCodeEnum.LOGIN_ERROR_BY_TOKEN_EXPIRED.getCode()).message(ResultCodeEnum.LOGIN_ERROR_BY_TOKEN_EXPIRED.getMessage());
                jsonAuthentication.WriteJSON(request, response, errorResp);
                chain.doFilter(request, response);
                return;
            }

        }
        chain.doFilter(request, response);
    }
}
