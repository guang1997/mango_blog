package com.myblog.service.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程安全的时间工具类
 *
 * @author 李斯特
 * @date 2022/03/12
 */
public class ThreadSafeDateFormat {
    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_PURE = "yyyyMMddHHmmss";
    public static final String DATETIME_MILL_PURE = "yyyyMMddHHmmssSSS";
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_PURE = "yyyyMMdd";
    public static final String TIME = "HH:mm:ss";
    public static final String TIME_PURE = "HHmmss";
    public static final String TIME_MILL_PURE = "HHmmssSSS";

    private static ThreadLocal<Map<String, DateFormat>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    public static DateFormat getDateFormat(String formatStr) {
        Map<String, DateFormat> threadLocalMap = threadLocal.get();
        DateFormat dateFormat = threadLocalMap.get(formatStr);
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(formatStr);
            threadLocalMap.put(formatStr, dateFormat);
        }
        return dateFormat;
    }

    /**
     * 将Date转换为对应格式的字符串
     * @param date
     * @param formatStr
     * @return
     */
    public static String format(Date date, String formatStr) {
        return getDateFormat(formatStr).format(date);
    }

    /**
     * 将对应格式的字符串解析为Date
     * @param strDate
     * @param formatStr
     * @return
     * @throws ParseException
     */
    public static Date parse(String strDate, String formatStr) throws ParseException {
        return getDateFormat(formatStr).parse(strDate);
    }

    /**
     * 将fromFormatStr格式的时间字符串转为toFormatStr格式
     * @param dateStr
     * @param fromFormatStr
     * @param toFormatStr
     * @return
     * @throws ParseException
     */
    public static String format(String dateStr, String fromFormatStr, String toFormatStr) throws ParseException {
        return format(parse(dateStr, fromFormatStr), toFormatStr);
    }
}
