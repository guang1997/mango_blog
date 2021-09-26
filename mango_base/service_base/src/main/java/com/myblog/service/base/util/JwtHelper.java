package com.myblog.service.base.util;



import com.myblog.service.base.common.ResultCodeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtHelper {

    private static Logger LOGGER = LoggerFactory.getLogger(JwtHelper.class);
    //过期时间，毫秒，24小时
    private static long tokenExpiration = 24*60*60*1000;
    //秘钥
    private static String tokenSignKey = "QYQ3b3QTEHfVcvOn183yj4br0Er2lk";

    private static Key getKeyInstance(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary(tokenSignKey);
        return new SecretKeySpec(bytes,signatureAlgorithm.getJcaName());
    }

    public static String createToken(JwtInfo jwtInfo){

        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("GULI-USER")//主题
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("id", jwtInfo.getId())//用户id
                .claim("nickname", jwtInfo.getNickname())//用户昵称
                .claim("avatar", jwtInfo.getAvatar())//用户头像
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();

        return JwtToken;
    }

    /**
     * 解析token
     * @param request
     * @return
     */
    public static Claims checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)) {
            throw new Exception(ResultCodeEnum.LOGIN_AUTH.getMessage());
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return claims;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new Exception(ResultCodeEnum.LOGIN_AUTH.getMessage());
        }
    }

    /**
     * 判断token是否有效
     * @param token
     * @return
     */
    public static boolean checkToken(String token) {
        if(StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据token获取会员id
     * @param request
     * @return
     */
    public static String getId(HttpServletRequest request) throws Exception {
        Claims claims = checkToken(request);
        return (String)claims.get("id");
    }

    /**
     * 根据token获取会员信息
     * @param request
     * @return
     */
    public static JwtInfo getJwtInfo(HttpServletRequest request) throws Exception {
        Claims claims = checkToken(request);
        return new JwtInfo(claims.get("id").toString(), claims.get("nickname").toString(), claims.get("avatar").toString());
    }
}
