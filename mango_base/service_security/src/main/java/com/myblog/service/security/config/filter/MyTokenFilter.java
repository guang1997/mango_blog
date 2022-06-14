/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.myblog.service.security.config.filter;

import com.fasterxml.jackson.core.filter.TokenFilter;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.config.entity.MySecurityProperties;
import com.myblog.service.security.config.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author /
 */
@Component
public class MyTokenFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MySecurityProperties properties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String headerToken = request.getHeader(Constants.ReplyField.HEADER);
        if (StringUtils.isBlank(headerToken)) {
            headerToken = request.getParameter(Constants.ReplyField.HEADER);
        }

        if (StringUtils.isNotBlank(headerToken)) {
            //postMan测试时，自动加入的前缀，要去掉。
            String token = headerToken.replace("Bearer", "").trim();
            LOGGER.info("get Token:{} from server", token);
            String secretKey = properties.getBase64Secret();
            try {
                boolean check = this.jwtTokenUtil.isExpiration(token, secretKey);
                // token已过期
                if (check) {
                    LOGGER.error("token:{} has expired",token);
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
                //通过令牌获取用户名称
                String username = jwtTokenUtil.getUsername(token, secretKey);
                LOGGER.info("get userName:{} from token", username);
                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //验证令牌有效性
                    boolean validata = jwtTokenUtil.validateToken(token, userDetails, secretKey);
                    // 令牌无效
                    if (!validata) {
                        LOGGER.error("token:{} validata:{}",token, validata);
                        Response.setResult(ResultCodeEnum.LOGIN_ERROR_BY_VALIDATA_TOKEN);
                        filterChain.doFilter(servletRequest, servletResponse);
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
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
