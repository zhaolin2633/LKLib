package cn.app.library.utils;

import android.util.Log;

/**
 * Description: Log工具类
 */
public class LogUtil {

    public static boolean isLogEnable = true;

    public static void setLogEnable(boolean enable) {
        isLogEnable = enable;
    }

    public static void logInfo(String tag, String info) {
        if (isLogEnable) {
            Log.i(tag, info);
        }
    }

    public static void logError(String tag, String info) {
        if (isLogEnable) {
            Log.e(tag, info);
        }
    }

}
