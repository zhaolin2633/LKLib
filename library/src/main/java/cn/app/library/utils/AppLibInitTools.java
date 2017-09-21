package cn.app.library.utils;


import android.app.Application;

import cn.app.library.widget.androidbootstrap.TypefaceProvider;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/15
 * desc : 描述：库初始化相关工作
 * </pre>
 */

public final class AppLibInitTools {
    private AppLibInitTools() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private AppLibInitTools(Builder builder) {
        appContext = builder.appContext;
        packageName = builder.packageName;
        isDebug = builder.isDebug;
        isShowToast = builder.isShowToast;
        init();
    }

    public static Application appContext;
    public static boolean isDebug = true;
    public static boolean isShowToast = true;
    public static String packageName = "";

    private void init() {
//        //相册初始化目录
//        InitAppDirHelp.getInstance().initAppDir(appContext, packageName);
//        //日志框架打印
//        Logger.addLogAdapter(new AndroidLogAdapter());
//        //dialog大全
//        StyledDialog.init(appContext);
//        //http
//        OkGo.getInstance().init(appContext);
        TypefaceProvider.registerDefaultIconSets();
    }


    public static final class Builder {
        private String packageName;
        private Application appContext;
        private boolean isDebug;
        private boolean isShowToast;

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder setApplication(Application appContext) {
            this.appContext = appContext;
            return this;
        }

        public Builder isDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public Builder isShowToast(boolean isShowToast) {
            this.isShowToast = isShowToast;
            return this;
        }

        public AppLibInitTools build() {
            return new AppLibInitTools(this);
        }
    }

}

