package cn.app.library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kairy on 2016/3/29.
 */
public class RexUtils {

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //判断name格式是否正确
    public static boolean isName(String name) {
        String str = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }


    /**
     * 秒数转时间
     */
    public static String formatSecond(Object second) {
        String html = "0秒";
        if (second != null) {
            Double s = Double.parseDouble(second.toString());
            String format;
            Object[] array;
            Integer hours = 0;
            Integer minutes = (int) (s / 60 - hours * 60);
            Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
            if (hours > 0) {
                format = "%1$,d时%2$,d分%3$,d秒";
                array = new Object[]{hours, minutes, seconds};
            } else if (minutes > 0) {
                format = "%1$,d分%2$,d秒";
                array = new Object[]{minutes, seconds};
            } else {
                format = "%1$,d秒";
                array = new Object[]{seconds};
            }
            html = String.format(format, array);
        }
        return html;
    }

    /**
     * 秒数转时间
     */
    public static String formatSeconds(Object second) {
        String html = "0秒";
        if (second != null) {
            Double s = Double.parseDouble(second.toString());
            Integer hours = 0;
            Integer minutes = (int) (s / 60 - hours * 60);
            Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
            if (minutes == 0) {
                html = "00";
            } else {
                html = String.valueOf(seconds);
            }
        }
        return html;
    }

    public static String formatMin(Object second) {
        String html = "0秒";
        if (second != null) {
            Double s = Double.parseDouble(second.toString());
            Integer hours = 0;
            Integer minutes = (int) (s / 60 - hours * 60);
            if (minutes == 0) {
                html = "00";
            } else {
                html = String.valueOf(minutes);
            }
        }
        return html;
    }

    /**
     * 时间戳转date
     */
    public static Date longTransDate(long timesLong) {
        return new Date(timesLong);
    }

    /**
     * 两个时间戳对比,
     *
     * @return 剩余秒数> 3600 返回 -1
     */
    public static int secondSurplus(long startLong, long endLong) {
        int result = -1;

        Date start = longTransDate(startLong);
        Date end = longTransDate(endLong);

        long diff = end.getTime() - start.getTime();   //两时间差，精确到毫秒

        long day = diff / (1000 * 60 * 60 * 24);          //差距多少天
        long hour = (diff / (60 * 60 * 1000) - day * 24);             //差距多少小时
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);    //差距多少分
        long secone = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  //差距多少秒

        if (day == 0) {
            if (hour == 0) {
                long surplus = min * 60 + secone;
                result = Integer.parseInt(String.valueOf(surplus)); //String.valueOf(surplus);
            }
        }
        return result;
    }

    /**
     * 两个时间戳对比,
     *
     * @return 剩余时间
     */
    public static String dateSurplus(long startLong, long endLong) {
        String result = "";

        Date start = longTransDate(startLong);
        Date end = longTransDate(endLong);

        long diff = end.getTime() - start.getTime();   //两时间差，精确到毫秒

        long day = diff / (1000 * 60 * 60 * 24);          //差距多少天
        long hour = (diff / (60 * 60 * 1000) - day * 24);             //差距多少小时
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);    //差距多少分
        long secone = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  //差距多少秒

        //<p><font color=\"#00bbaa\">颜色2</p>
        result = "距开始还有<font color=\"#FF5722\">" + day + "</font>天";
        String s = String.valueOf(day);
        if (day == 0) {
            result = "距开始还有<font color=\"#FF5722\">" + hour + "</font>小时";
        }
        return result;
    }

    /**
     * 两个时间戳对比,
     *
     * @return 剩余时间
     */
    public static String dateDay(long startLong, long endLong) {
        Date start = longTransDate(startLong);
        Date end = longTransDate(endLong);
        long diff = end.getTime() - start.getTime();   //两时间差，精确到毫秒
        long day = diff / (1000 * 60 * 60 * 24);          //差距多少天
        long hour = (diff / (60 * 60 * 1000) - day * 24);             //差距多少小时
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);    //差距多少分
        long secone = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  //差距多少秒
        String s = String.valueOf(day);
        return s;
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        boolean installed = false;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo in : installedApplications) {
            if (packageName.equals(in.packageName)) {
                installed = true;
                break;
            } else {
                installed = false;
            }
        }
        return installed;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber 电话号码
     */
    public static void call(@NonNull Context context, @NonNull String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static String getMoneyString(double value) {
        return String.format("%.2f", value);
    }

    public static String getRMBMoneyString(double value) {
        return String.format("￥%.2f", value);
    }

    public static String getTime(int time) {
        return String.format("%02d", time);
    }

    /**
     * @param originalUrl json得到的图片链接
     * @return formatedUrl 切图链接
     * @author Tiger
     */
    public static String getSmallImageUrl(String originalUrl) {
        if (originalUrl.endsWith("@!120")) {
            return originalUrl;
        }
        return originalUrl + "@!120";
    }

    /**
     * @param originalUrl json得到的图片链接
     * @return formatedUrl 切图链接
     * @author Tiger
     */
    public static String getBigImageUrl(String originalUrl) {
        if (originalUrl.endsWith("@!600")) {
            return originalUrl;
        }
        return originalUrl + "@!600";
    }

    public static void openUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        context.startActivity(intent);
    }

    public static void openInputMethod(Context context, View editText) {
        if (context != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, 0);
        }
    }

    public static void closeInputMethod(Context context, View editText) {
        if (context != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

}
