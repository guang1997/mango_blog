package com.myblog.service.web.util;

import com.myblog.service.web.entity.dto.UniqueKeyDto;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class UniqueKeyUtil {

    public static String getUniqueKey(HttpServletRequest request, String ipAddr, UniqueKeyDto uniqueKey) {
        String agent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(agent)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            uniqueKey.setName(operatingSystem.getName());
            uniqueKey.setDeviceType(operatingSystem.getDeviceType().toString());
            uniqueKey.setGroup(operatingSystem.getGroup().toString());
            uniqueKey.setManufacturer(operatingSystem.getManufacturer().toString());
        }
        uniqueKey.setIp(ipAddr);

        return uniqueKey.toString();
    }
}
