package cn.app.library.widget.toast;

import android.view.View;
import android.widget.Toast;

import cn.app.library.utils.AppLibInitTools;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/15
 * desc :
 * </pre>
 */
public class ToastUtil {
    public enum ToastType {
        SUCCESS, ERROR, WARN, INFO, NORMAL_TEXT
    }

    public static void show(ToastType type, CharSequence text) {
        switch (type) {
            case SUCCESS:
                success(text);
                break;
            case ERROR:
                error(text);
                break;
            case WARN:
                warn(text);
                break;
            case INFO:
                info(text);
                break;
            case NORMAL_TEXT:
                show(text);
                break;
        }
    }

    public static void success(CharSequence text) {
        Toasty.success(AppLibInitTools.appContext, text, Toast.LENGTH_SHORT, true).show();
    }

    public static void error(CharSequence text) {
        Toasty.error(AppLibInitTools.appContext, text, Toast.LENGTH_SHORT, true).show();
    }

    public static void warn(CharSequence text) {
        Toasty.warning(AppLibInitTools.appContext, text, Toast.LENGTH_SHORT, true).show();
    }

    public static void info(CharSequence text) {
        Toasty.info(AppLibInitTools.appContext, text, Toast.LENGTH_SHORT, true).show();
    }

    public static void show(CharSequence text) {
        Toasty.normal(AppLibInitTools.appContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void show(CharSequence text, int resId) {
        Toasty.normal(AppLibInitTools.appContext, text, AppLibInitTools.appContext.getResources().getDrawable(resId)).show();
    }

    public static void showSnackbar(String text, View view) {
        SnackbarUtil.ShortSnackbar(view, text, SnackbarUtil.Info).show();
    }

    public static void showSnackbar(String text, View view, int messageColor, int backgroundColor) {
        SnackbarUtil.IndefiniteSnackbar(view, "我是弹出的信息", 3000, messageColor, backgroundColor).show();
    }
}
