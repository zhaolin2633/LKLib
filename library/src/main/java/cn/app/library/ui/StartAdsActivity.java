package cn.app.library.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;
import cn.app.library.http.config.Constants;
import cn.app.library.service.AdEntity;
import cn.app.library.utils.SerializableUtils;
import cn.app.library.widget.glideimageview.GlideImageLoader;


/**
 * Describe：启动页广告
 */
public abstract class StartAdsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private AdEntity mAdEntity;
    private ImageView mImageView;
    private Button mBtnSkip;
    private CountDownTimer countDownTimer = new CountDownTimer(3200, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnSkip.setText("跳过(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            mBtnSkip.setText("跳过(" + 0 + "s)");
            toMainActivity();
        }
    };

    /**
     * 去主页
     */
    public abstract void toMainActivity();

    @Override
    protected int getContentView() {
        return R.layout.activity_start_ads;
    }

    @Override
    protected void initView() {
        mImageView = findView(R.id.spash_image);
        mBtnSkip = findView(R.id.btn_skip);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSplash();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void showSplash() {
        mAdEntity = getLocalSplash();
        if (mAdEntity != null && mAdEntity.pic != null && !TextUtils.isEmpty(mAdEntity.pic)) {
            GlideImageLoader.create(mImageView).loadImage(mAdEntity.pic, R.drawable.loading_default);
            startClock();
        } else {
            mBtnSkip.setVisibility(View.GONE);
            mBtnSkip.setOnClickListener(null);
            mImageView.setOnClickListener(null);
            mImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toMainActivity();
                }
            }, 1000);
        }
    }

    private void startClock() {
        mBtnSkip.setVisibility(View.VISIBLE);
        mBtnSkip.setOnClickListener(StartAdsActivity.this);
        mImageView.setOnClickListener(StartAdsActivity.this);
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_skip) {
            toMainActivity();
            return;
        } else if (i == R.id.spash_image) {
            if (mAdEntity != null && mAdEntity.uri != null && !TextUtils.isEmpty(mAdEntity.uri)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("start_advertise", mAdEntity);
                toMainActivity();
            }
            return;
        }
    }

    @Override
    protected void setStatusBar() {
        //设置状态栏透明
        StatusBarUtil.setTranslucent(this, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
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

}
