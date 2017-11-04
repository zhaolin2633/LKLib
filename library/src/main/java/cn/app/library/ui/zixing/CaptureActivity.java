package cn.app.library.ui.zixing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;


/**
 * @describe: ZiXing二维码扫描
 */

public class CaptureActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CaptureActivity";
    private LinearLayout llBack;
    public static final int INTENT_REQUEST_CODE_CAPTURE = 11000;
    private CaptureFragment captureFragment;

    public static void start(Context ct) {
        ((Activity) ct).startActivityForResult(new Intent(ct, CaptureActivity.class), CaptureActivity.INTENT_REQUEST_CODE_CAPTURE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initView() {
        llBack = findView(R.id.capture_title);
    }

    @Override
    protected void initData() {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    protected void setListener() {
        llBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.capture_title) {
            finish();
            return;
        }
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.common_main), 0);
    }

    @Override
    protected int getStatusBarId() {
        return 0;
    }

    @Override
    protected int getStatusBarBgId() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }
    };
}
