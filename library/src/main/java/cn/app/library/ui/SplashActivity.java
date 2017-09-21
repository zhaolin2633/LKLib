package cn.app.library.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;
import cn.app.library.http.config.Constants;
import cn.app.library.service.AdEntity;
import cn.app.library.service.SplashDownLoadService;
import cn.app.library.utils.AppManager;
import cn.app.library.utils.AppUtils;
import cn.app.library.utils.HandlerTip;
import cn.app.library.utils.SPUtils;
import cn.app.library.utils.SerializableUtils;


/**
 * @describe: 启动页
 */

public abstract class SplashActivity extends BaseAppCompatActivity {

    private static final String TAG = "SplashActivity";

    private LinearLayout mLlSplash;

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (savedInstanceState != null) {
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }
        //解析从外部浏览器打开app
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            int activitySize = AppManager.getInstance().getSize();

            finish();
        } else {
            showMain();
        }

    }

    @Override
    protected void initView() {
        mLlSplash = findView(R.id.ll_splash);
    }

    private void showMain() {
        mLlSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstRun()) {
                    goToActivity(GuideActivity.class, true);
                } else {
                    jumpMainActivity();
                }
            }
        }, 1000);
    }

    private void jumpMainActivity() {

        showAndDownSplash();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /**
         * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
         * 场景：点击通知栏跳转到此，会收到Intent
         */
        setIntent(intent);
    }

    /**
     * 判断是否是第一次启动程序
     *
     * @return
     */
    private boolean isFirstRun() {
        if (SPUtils.getInt(this, "welcome_version", 0) < AppUtils.getAppCode(this)) {
            SPUtils.putInt(this, "welcome_version", AppUtils.getAppCode(this));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void setStatusBar() {
        //设置状态栏透明
        StatusBarUtil.setTranslucent(this, 0);
    }

    private void showMainActivity(Intent intent) {
        toMainActivity();
        finish();
    }

    /**
     * 去主页
     */
    public abstract void toMainActivity();

    /**
     * 去引导页面
     */
    public abstract void toGuideActivity();

    /**
     * 去广告页面
     */
    public abstract void toAdActivity();

    /**
     * 去登录
     */
    public abstract void toLoginActivity();

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandlerTip.getInstance().removeHandler();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    private void showAndDownSplash() {
        if (getLocalSplash() != null) {
            goToActivity(StartAdsActivity.class, true);
        } else {
            toMainActivity();
        }
        SplashDownLoadService.startDownLoadSplashImage(this, Constants.DOWNLOAD_SPLASH);
    }


    private AdEntity getLocalSplash() {
        AdEntity splash = null;
        try {
            File serializableFile = SerializableUtils.getSerializableFile(Constants.SPLASH_PATH,
                    Constants.SPLASH_FILE_NAME);
            splash = (AdEntity) SerializableUtils.readObject(serializableFile);
        } catch (IOException e) {
            Logger.i("Splash", "SplashActivity 获取本地序列化闪屏失败" + e.getMessage());
        }
        return splash;
    }

    /**
     * 屏蔽返回键，防止用户误触退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
