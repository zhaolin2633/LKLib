package cn.app.library.utils;

import java.security.MessageDigest;

/**
 * Author：xiaohaibin
 * Time：2017/3/15
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：Signature加密工具类
 */

public class SignatureUtil {

    /**
     * 无AppSecert字段
     *
     * @param appRandom 长度为0-10的随机数
     * @param appTime   本地时间戳
     * @return
     */
    public static String NoSignUtil(String appRandom, String appTime) {
        String sha1 = getSHA1(appRandom+ appTime + "57b83d9c5873");
        return sha1;
    }

    /**
     * 有AppSecert字段
     *
     * @param appRandom 长度为0-10的随机数
     * @param appTime   本地时间戳
     * @param appSecert AppSecert
     * @return
     */
    public static String SignUtil(String appRandom, String appTime, String appSecert) {
        return getSHA1(appRandom + appTime + appSecert + "57b83d9c5873");
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    public static String getSHA1(String requestBody) {
        return encode("sha1", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes("UTF-8"));
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte aByte : bytes) {

            int value = aByte;
            if (value < 0) {
                value = 256 + value;
            }
            int d1 = value / 16;
            int d2 = value % 16;
            buf.append(HEX_DIGITS[d1]);
            buf.append(HEX_DIGITS[d2]);

        }
        return buf.toString();
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
