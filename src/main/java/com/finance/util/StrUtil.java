package com.finance.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 */
public final class StrUtil {
    
    /**
     * 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块
     */
    private static final String US_ASCII = "US-ASCII";

    /**
     * ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1
     */
    private static final String ISO_8859_1 = "ISO-8859-1";

    /**
     * 8 位 UCS 转换格式
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序
     */
    private static final String UTF_16BE = "UTF-16BE";

    /**
     * 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序
     */
    private static final String UTF_16LE = "UTF-16LE";

    /**
     * 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识
     */
    private static final String UTF_16 = "UTF-16";

    /**
     * 中文超大字符集
     */
    private static final String GBK = "GBK";


    /**
     * 将字符编码转换成GBK码
     *
     * @return String
     */
    public final static String toGBK(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, GBK);
    }

    /**
     * 将字符编码转换成UTF-8码
     *
     * @return String
     */
    public final static String toUTF8(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, UTF_8);
    }

    /**
     * 将字符编码转换成US-ASCII码
     *
     * @return String
     */
    public final static String toASCII(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, US_ASCII);
    }

    /**
     * 将字符编码转换成UTF-16码
     *
     * @return String
     */
    public final static String toUTF16(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, UTF_16);
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param sStr        待转换编码的字符串
     * @param sNewCharset 目标编码
     * @return String
     */
    public final static String changeCharset(String sStr, String sNewCharset) throws UnsupportedEncodingException {
        // 用默认字符编码解码字符串。
        byte[] aBits = sStr.getBytes();
        // 用新的字符编码生成字符串
        return new String(aBits, sNewCharset);
    }

    /**
     * 将字符编码转换成ISO-8859-1码
     *
     * @return String
     */
    public final static String toISO_8859_1(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, ISO_8859_1);
    }

    /**
     * 将字符编码转换成UTF-16BE码
     *
     * @return String
     */
    public final static String toUTF16BE(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, UTF_16BE);
    }

    /**
     * 将字符编码转换成UTF-16LE码
     *
     * @return String
     */
    public final static String toUTF16LE(String sStr) throws UnsupportedEncodingException {
        return changeCharset(sStr, UTF_16LE);
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param sStr        待转换编码的字符串
     * @param sOldCharset 原编码
     * @param sNewCharset 目标编码
     * @return String
     */
    public final static String changeCharset(String sStr, String sOldCharset, String sNewCharset)
            throws UnsupportedEncodingException {
        // 用旧的字符编码解码字符串。解码可能会出现异常。
        byte[] aBits = sStr.getBytes(sOldCharset);
        // 用新的字符编码生成字符串
        return new String(aBits, sNewCharset);
    }

    /**
     * 返回正则表达式的结果集
     *
     * @return List<String>
     */
    public final static List<String> getRegexResult(String sStr, String pattern) {
        ArrayList<String> aList = new ArrayList<String>();
        Pattern oPattern = Pattern.compile(pattern);
        Matcher matcher = oPattern.matcher(sStr);
        while (matcher.find()) {
            for (int i = 1, nTotal = matcher.groupCount(); i <= nTotal; i++) {
                aList.add(matcher.group(i));
            }
        }
        return aList;
    }

    /**
     * 字符串正则表达式替换
     *
     * @return String
     */
    public final static String getRegexReplaceResult(String sSource, String sReplace, String pattern) {
        Pattern oPattern = Pattern.compile(pattern);
        Matcher matcher = oPattern.matcher(sSource);
        return matcher.replaceAll(sReplace);
    }

    /**
     * 正则表达式检查结果
     *
     * @return boolean
     */
    public final static boolean checkMather(String sStr, String pattern) {
        Pattern oPattern = Pattern.compile(pattern);
        Matcher matcher = oPattern.matcher(sStr);
        return matcher.matches();
    }

    /**
     * 字符串 ： 解决数据库入库时候的非法符号 转 HTML 符号
     */
    public final static String toHtml(Object sStr, String sDefault) {
        String sValue = sStr == null ? sDefault : sStr.toString();
        sValue = sValue.replaceAll("<", "&lt;");
        sValue = sValue.replaceAll(">", "&gt;");
        sValue = sValue.replaceAll("&", "&amp;");
        return sValue;
    }

    /**
     * 获取八位不重复随机码（取当前时间戳转化为16进制）
     */
    public final static String randomCode(long time) {
        return Integer.toHexString((int) time);
    }

    public static String getVerificationCode(int count, Boolean isLetter) {
        String[] beforeLetterShuffle = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] beforeNumberShuffle = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "8"};
        List<String> list = Arrays.asList(isLetter ? beforeLetterShuffle : beforeNumberShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(0, count);
        return result;
    }

    /**
     * 判断在values数组中是否存在value元素
     *
     * @param values 数组
     * @param value  字符串
     * @return 存在，返回true。反之，false。
     */
    public static boolean contains(String[] values, String value) {
        if (values != null && values.length > 0) {
            for (String s : values) {
                if (s != null) {
                    if (s.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 十六进制格式打印字节数组
     */
    public static String hexFormat(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        for (byte b : data) {
            if (n % 16 == 0) {
                stringBuilder.append(String.format("%05X: ", n));
            }
            stringBuilder.append(String.format("%02X ", b));
            n++;
            if (n % 16 == 0) {
                stringBuilder.append("\n");
            }
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
