package cn.app.library.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.app.library.base.ContainerFragmentActivity;

/**
 * Author:Tiger
 * <p>
 * CrateTime:2016-12-02 11:43
 * <p>
 * Description:跳转工具类，仅针对单Activity多Fragment的模式使用
 */
public class JumpUtil {

    public static void jump(Context context, String className, String title) {
        jump(context, className, title, null);
    }

    public static void jump(Context context, String className, String title, Bundle arguments) {
        jump(context, className, title, arguments, false);
    }

    public static void jump(Context context, String className, String title, Bundle arguments, boolean fullScreen) {
        Intent intent = new Intent(context, ContainerFragmentActivity.class);
        intent.putExtra("className", className);
        intent.putExtra("title", title);
        intent.putExtra("fullScreen", fullScreen);
        if (arguments != null) {
            intent.putExtra("arguments", arguments);
        }
        context.startActivity(intent);
    }

    public static void jumpForResult(final Fragment fragment, String className, String title, int requestCode) {
        jumpForResult(fragment, className, title, null, requestCode);
    }

    public static void jumpForResult(final Fragment fragment, String className, String title, Bundle arguments, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), ContainerFragmentActivity.class);
        intent.putExtra("className", className);
        intent.putExtra("title", title);
        if (arguments != null) {
            intent.putExtra("arguments", arguments);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static class BundleBuilder {

        private Bundle bundle;

        private BundleBuilder() {
            bundle = new Bundle();
        }

        public static BundleBuilder newBuilder() {
            return new BundleBuilder();
        }

        public BundleBuilder putString(String key, String value) {
            bundle.putString(key, value);
            return this;
        }

        public BundleBuilder putBoolean(String key, boolean value) {
            bundle.putBoolean(key, value);
            return this;
        }

        public BundleBuilder putInt(String key, int value) {
            bundle.putInt(key, value);
            return this;
        }

        public BundleBuilder putLong(String key, long value) {
            bundle.putLong(key, value);
            return this;
        }

        public BundleBuilder putDouble(String key, double value) {
            bundle.putDouble(key, value);
            return this;
        }

        public Bundle getBundle() {
            return bundle;
        }

    }

}
