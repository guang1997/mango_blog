package com.myblog.service.security.config.util;


import com.myblog.service.base.common.Constants;
import com.myblog.service.security.config.entity.AuthUser;
import com.myblog.service.security.config.entity.vo.AdminVO;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.Role;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

/**
 * JWT工具类
 *
 * @author 李斯特
 * @date 2021/10/02
 */
@Component
public class JwtTokenUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * 构建token
     *
     * @return
     */
    public String createToken(Admin admin, List<Role> roles, long expiresSecond, String secretKey) {
        String userName = admin.getUsername();
        String avatar = admin.getAvatar();
        String id = admin.getId();
        StringBuilder roleNames = new StringBuilder();
        for (Role role : roles) {
            roleNames.append(role.getRoleName()).append(Constants.Symbol.COMMA);
        }
        roleNames.substring(0, roleNames.length() - 2);

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim(Constants.ReplyField.ID, id)
                .claim(Constants.ReplyField.AVATAR, avatar)
                .claim(Constants.ReplyField.ROLES, roleNames.toString())
                .setSubject(userName)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (expiresSecond >= 0) {
            long expMillis = nowMillis + expiresSecond;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }

    /**
     * 解析token
     */
    public Claims parseToken(String token, String secretKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @param secretKey
     * @return
     */
    public boolean isExpiration(String token, String secretKey) {
        if (parseToken(token, secretKey) == null) {
            return true;
        } else {
            return parseToken(token, secretKey).getExpiration().before(new Date());
        }
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails, String secretKey) throws Exception {
        String username = getUsername(token, secretKey);
        return (username.equals(userDetails.getUsername()) && !isExpiration(token, secretKey));
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsername(String token, String secretKey) {
        String username = null;
        try {
            Claims claims = parseToken(token, secretKey);
            username = claims.getSubject();
        } catch (Exception e) {
            LOGGER.error("getUsername from token error, Exception:", e);
        }
        return username;
    }

    /**
     * 根据token获取单条信息
     *
     * @param token
     * @return
     */
    public Object getMessage(String token, String secretKey, String type) throws Exception {
        Object result = null;
        try {
            Claims claims = parseToken(token, secretKey);
            result = claims.get(type);
        } catch (Exception e) {
            LOGGER.error("getMessage from token error, Exception:", e);
        }
        return result;
    }

    /**
     * 根据token获取string类型的单条信息
     *
     * @param token
     * @return
     */
    public String getStrMessage(String token, String secretKey, String type) throws Exception {
        Object result = getMessage(token, secretKey, type);
        if (result == null) {
            LOGGER.error("getStrMessage from token error, result is null");
            return null;
        }
        if (!(result instanceof String)) {
            LOGGER.error("getStrMessage from token error, type:{} is not string", type);
            return null;
        }
        return String.valueOf(result);
    }

    /**
     * 根据token获取int类型的单条信息
     *
     * @param token
     * @return
     */
    public Integer getIntMessage(String token, String secretKey, String type) throws Exception {
        Object result = getMessage(token, secretKey, type);
        if (result == null) {
            LOGGER.error("getIntMessage from token error, result is null");
            return null;
        }
        if (!(result instanceof Integer)) {
            LOGGER.error("getIntMessage from token error, type:{} is not int", type);
            return null;
        }
        return (Integer) result;
    }

    /**
     * 根据token获取所有信息
     *
     * @param token
     * @return
     */
    public AuthUser getAuthUser(String token, String secretKey) throws Exception {
        Claims claims = parseToken(token, secretKey);
        String roles = String.valueOf(claims.get(Constants.ReplyField.ROLES));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles.split(Constants.Symbol.COMMA)) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new AuthUser(claims.get(Constants.ReplyField.ID).toString(),
                claims.get(Claims.SUBJECT).toString(),
                null,
                claims.get(Constants.ReplyField.AVATAR).toString(),
                (Integer) claims.get(Claims.EXPIRATION),
                authorities);
    }
}
