package com.finance.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    /**
     * 日期对象转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            result = sdf.format(date);
        }
        return result;
    }

    /**
     * 字符串转日期对象
     *
     * @param str
     * @param format
     * @return
     * @throws Exception
     */
    public static Date formatString(String str, String format) throws Exception {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    public static String getCurrentDateStr() throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(date);
    }

    /**
     * 当前日期．年 ＋ 当前日期．月 ＋ 当前日期．日
     *
     * @return
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }


    /**
     * 当前日期．年 ＋ 当前日期．月 ＋ 当前日期．日 ＋ 当前日期．时 ＋ 当前日期．分 ＋ 当前日期．秒
     *
     * @return
     */
    public static String getFullTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(cal.getTime());
    }
}
