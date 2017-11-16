package cn.app.library.ui.zixing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;
import cn.app.library.picture.lib.PictureSelector;
import cn.app.library.picture.lib.config.PictureConfig;
import cn.app.library.picture.lib.entity.LocalMedia;
import cn.app.library.qrcode.QRCodeDecoder;
import cn.app.library.qrcode.ZXingView;
import cn.app.library.qrcode.core.QRCodeView;
import cn.app.library.utils.PictureUtils;


/**
 * @describe: ZiXing二维码扫描
 */

public abstract class CaptureActivity extends BaseAppCompatActivity implements View.OnClickListener, QRCodeView.Delegate {

    private static final String TAG = "CaptureActivity";
    public RelativeLayout llBack;
    public ZXingView mQRCodeView;
    public LinearLayout ll_bottom;
    public LinearLayout ll_scan;
    public ImageView iv_scan;
    public TextView tv_scan;
    public LinearLayout ll_photo;
    public ImageView iv_photo;
    public TextView tv_photo;

    public static final int INTENT_REQUEST_CODE_CAPTURE = 11000;


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
        ll_bottom = findView(R.id.ll_bottom);
        ll_scan = findView(R.id.ll_scan);
        iv_scan = findView(R.id.iv_scan);
        tv_scan = findView(R.id.tv_scan);
        ll_photo = findView(R.id.ll_photo);
        iv_photo = findView(R.id.iv_photo);
        tv_photo = findView(R.id.tv_photo);
        mQRCodeView = findView(R.id.zxingview);
        mQRCodeView.setDelegate(this);
    }

    public void setLlBottomView(boolean isShow) {
        if (ll_bottom != null)
            ll_bottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setLlScanBtn(int resId, String text, int textColorId) {
        ll_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSacn(v);
            }
        });
        if (resId > 0)
            iv_scan.setImageResource(resId);
        if (!TextUtils.isEmpty(text))
            tv_scan.setText(text);
        if (textColorId > 0)
            tv_scan.setTextColor(ContextCompat.getColor(this, textColorId));
    }

    public void setllPhotoBtn(int resId, String text, int textColorId) {
        ll_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
                clickPhoto(v);
            }
        });
        if (resId > 0)
            iv_photo.setImageResource(resId);
        if (!TextUtils.isEmpty(text))
            tv_photo.setText(text);
        if (textColorId > 0)
            tv_photo.setTextColor(ContextCompat.getColor(this, textColorId));
    }

    public abstract void clickSacn(View view);

    public abstract void clickPhoto(View view);

    @Override
    protected void initData() {
        setLlScanBtn(0, "", 0);
        setllPhotoBtn(0, "", 0);
    }

    /**
     * 选择图片
     */
    private void choosePhoto() {
        PictureUtils.getInstance().init(this, 1, R.style.picture_Sina_style);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mQRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            // 图片选择
            List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
            final String picturePath = localMedias.get(0).getPath();

            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new SacnAsyncTask(picturePath).execute();
        }
    }

    public class SacnAsyncTask extends AsyncTask<Void, Void, String> {
        String picturePath;

        public SacnAsyncTask(String picturePath) {
            this.picturePath = picturePath;
        }

        @Override
        protected String doInBackground(Void... params) {
            showLoding("正在识别");
            return QRCodeDecoder.syncDecodeQRCode(picturePath);
        }

        @Override
        protected void onPostExecute(String result) {
            dismissLoading();
            if (TextUtils.isEmpty(result)) {
                showToast("未发现二维码");
            } else {
                scanResult(result);
            }
        }
    }

    @Override
    protected void setListener() {
        llBack.setOnClickListener(this);
    }

    public abstract void scanResult(String result);

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.capture_title) {
            finish();
            return;
        }
    }

    public void setLlBackColor(int coloriD) {
        if (llBack != null) {
            llBack.setBackgroundColor(ContextCompat.getColor(this, coloriD));
        }
    }

    public void setLlBackResource(int resiD) {
        if (llBack != null) {
            llBack.setBackgroundResource(resiD);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
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

}
