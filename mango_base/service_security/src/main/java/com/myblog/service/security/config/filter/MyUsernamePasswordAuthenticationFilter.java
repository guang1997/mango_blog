package com.myblog.service.security.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.IpUtils;
import com.myblog.service.base.util.RedisUtil;
import com.myblog.service.security.config.util.JSONAuthentication;
import com.myblog.service.security.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 重写UsernamePasswordAuthenticationFilter过滤器
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(MyUsernamePasswordAuthenticationFilter.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JSONAuthentication jsonAuthentication;

    @Value("${auth.loginLimitCount}")
    private Integer loginLimitCount;

    public MyUsernamePasswordAuthenticationFilter() {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/auth/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {

        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {


            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            //取authenticationBean
            Map<String, String> authenticationBean = null;
            try (InputStream is = request.getInputStream()) {
                authenticationBean = mapper.readValue(is, Map.class);
                // 校验用户是否失败次数过多
                String ipAddress = IpUtils.getIpAddr(request);
                String limitCount = redisUtil.get(RedisConstants.LOGIN_LIMIT + RedisConstants.DIVISION + ipAddress);
                if (StringUtils.isNotEmpty(limitCount)) {
                    Integer tempLimitCount = Integer.valueOf(limitCount);
                    if (tempLimitCount >= loginLimitCount) {
                        Response errResp = Response
                                .error()
                                .code(ResultCodeEnum.LOGIN_LOCKED.getCode()).message(ResultCodeEnum.LOGIN_LOCKED.getMessage());
                        jsonAuthentication.WriteJSON(request, response, errResp);
                        return null;
                    }
                }
                if (!authenticationBean.isEmpty()) {
                    //获得账号、密码
                    String username = authenticationBean.get(SPRING_SECURITY_FORM_USERNAME_KEY);
                    String password = authenticationBean.get(SPRING_SECURITY_FORM_PASSWORD_KEY);

                    //检测账号、密码是否存在
                    if (adminService.checkLogin(username, password)) {
                        //将账号、密码装入UsernamePasswordAuthenticationToken中
                        authRequest = new UsernamePasswordAuthenticationToken(username, password);
                        setDetails(request, authRequest);
                        return this.getAuthenticationManager().authenticate(authRequest);
                    } else {
                        Response errResp = Response
                                .error()
                                .code(ResultCodeEnum.LOGIN_ERROR_LOCKED.getCode()).message(String.format(ResultCodeEnum.LOGIN_ERROR_LOCKED.getMessage(), setLoginCommit(ipAddress)));
                        jsonAuthentication.WriteJSON(request, response, errResp);
                        return null;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("attemptAuthentication failed, Exception:", e);
                throw new RuntimeException(e);
            }
            return null;
        } else {
            return this.attemptAuthentication(request, response);
        }
    }

    /**
     * 设置登录次数
     *
     * @param ipAddress
     * @return
     */
    private Integer setLoginCommit(String ipAddress) {
        String key = RedisConstants.LOGIN_LIMIT + RedisConstants.DIVISION + ipAddress;
        String loginCountStr = redisUtil.get(key);
        Integer limitCount = this.loginLimitCount;
        if (StringUtils.isEmpty(loginCountStr)) {
            redisUtil.setEx(key, String.valueOf(--limitCount), 30, TimeUnit.MINUTES);
        } else {
            int loginCount = Integer.parseInt(loginCountStr);
            limitCount -= ++loginCount;
            redisUtil.setEx(key, String.valueOf(limitCount), 30, TimeUnit.MINUTES);
        }
        return limitCount;
    }
}
