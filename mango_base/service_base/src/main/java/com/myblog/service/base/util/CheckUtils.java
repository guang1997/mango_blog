package com.myblog.service.base.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验相关工具类
 *
 * @author 李斯特
 * @date 2021/09/28
 */
public class CheckUtils {

    private static final String CHECK_EMAIL_REGEX = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final String CHECK_MOBILE_REGEX = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|17[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    /**
     * 校验邮箱
     * @param email
     * @return
     */
    public static boolean checkEmails(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        boolean isEmail = false;
        try {
            Pattern pattern = Pattern.compile(CHECK_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            isEmail = matcher.matches();
        } catch (Exception e) {
            isEmail = false;
        }

        return isEmail;
    }

    /**
     * 校验手机
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        boolean isMobile = false;
        try {
            Pattern pattern = Pattern.compile(CHECK_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(mobile);
            isMobile = matcher.matches();
        } catch (Exception e) {
            isMobile = false;
        }

        return isMobile;
    }
}
