package cn.app.library.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import cn.app.library.BuildConfig;
import cn.app.library.utils.AppLibInitTools;
import cn.app.library.utils.AppManager;

/**
 * Author：xiaohaibin
 * Time：2017/6/22
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：
 */
public abstract class BaseApplication extends Application {
    private static BaseApplication instance;
    private Context context;
    public static BaseApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        if (instance == null) {
            instance = this;
        }
        if (context == null) {
            context = getApplicationContext();
        }
        //使用OkLib库必须先调用初始化方法
        new AppLibInitTools.Builder()
                .setApplication(this)
                .setPackageName(BuildConfig.APPLICATION_ID)
                .isDebug(true)
                .isShowToast(true)
                .build();
    }

    public Context getContext() {
        return context;
    }

    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.getInstance().pushActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            AppManager.getInstance().killActivity(activity);
        }
    };

    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onTerminate();
    }
}
