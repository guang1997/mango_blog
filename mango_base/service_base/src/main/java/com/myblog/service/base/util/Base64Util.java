package com.myblog.service.base.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Base64Util {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * base64编码
     * @param src
     * @return
     */
    public static String encodeToString(byte[] src) {
        if (Objects.isNull(src)) {
            return null;
        }
        if (Objects.equals(src.length, 0)) {
            return "";
        }
        return Base64.encodeBase64String(src);
    }

    /**
     * base64解码
     * @param base64Str
     * @return
     */
    public static String decodeToString(String base64Str) {
        if (StringUtils.isBlank(base64Str)) {
            return null;
        }
        if (Objects.equals(base64Str.length(), 0)) {
            return "";
        }
        return new String(Base64.decodeBase64(base64Str), DEFAULT_CHARSET);
    }
}
