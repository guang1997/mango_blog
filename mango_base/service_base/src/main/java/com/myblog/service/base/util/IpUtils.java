package com.myblog.service.base.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP相关工具类
 *
 * @author 李斯特
 * @date 2021/09/27
 */
public class IpUtils {

    /**
     * 获取当前ip地址
     *
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) throws UnknownHostException {
        String ipAddress = request.getHeader("x-forwarded-for");//配置代理情况，配置代理后header信息通过x-forwarded-for标记真实ip地址

        if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = request.getHeader("Proxy-Client-IP");//整合apache+Weblogic 代理情况
        }

        if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");//整合apache+Weblogic 代理情况
        }

        if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
            ipAddress = request.getRemoteAddr();//获取未配置代理的情况
        }
        // 获取本机真实地址
        if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
            InetAddress addr = InetAddress.getLocalHost();
            ipAddress = addr.getHostAddress();
        }
        // 如果配置了多层代理，取第一个ip
        if (ipAddress != null && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }

    private static String getIp(String ip) {
        if ((ip != null) && (ip.indexOf(44) > 0)) {
            String[] ipArray = ip.split(",");

            ip = ipArray[(ipArray.length - 1)].trim();
        }
        return ip;
    }
}
