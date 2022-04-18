package com.myblog.service.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public static final String YEAR = "yyyy";
    public static final String DATE_PURE_SPLIT = "yyyy/MM/dd";
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

    /**
     * 获取今天开始的时间
     * @return
     */
    public static String getTodayStartTime() {
        DateFormat format = getDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 获取今天结束的时间
     * @return
     */
    public static String getTodayEndTime() {
        DateFormat format = getDateFormat("yyyy-MM-dd 23:59:59");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 获取距离date day天的日期
     * @param date
     * @param day
     * @return
     * @throws ParseException
     */
    public static String getDate(String date, int day) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFormat(DATETIME).parse(date));
        cal.add(Calendar.DAY_OF_MONTH, day);
        return format(cal.getTime(), DATETIME);
    }

    /**
     * 获取某个时间段内所有日期
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static List<String> getDayBetweenDates(String beginTime, String endTime) throws ParseException {
        Date beginDate = parse(beginTime, DATE);
        Date endDate = parse(endTime, DATE);
        List<String> dateList = new ArrayList<>();
        dateList.add(format(beginTime,DATETIME, DATE));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);
        // 测试此日期是否在指定日期之后
        while (endDate.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(format(calBegin.getTime(), DATE));
        }
        return dateList;
    }

    /**
     * 获取当前月第一天
     * @return
     */
    public static String getFirstDayOfMonth() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format(cale.getTime(), "yyyy-MM-dd 00:00:00");
    }

    public static void main(String[] args) {
        System.out.println(getFirstDayOfMonth());
    }
}
