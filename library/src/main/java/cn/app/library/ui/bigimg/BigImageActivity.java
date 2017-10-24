package cn.app.library.ui.bigimg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;
import cn.app.library.utils.RxImage;
import cn.app.library.widget.toast.ToastUtil;



/**
 * @describe: 详情页查看大图
 */

public class BigImageActivity extends BaseAppCompatActivity {

    private static final String TAG = "BigImageActivity";
    public static final String IMAGES = "images";
    public static final String IMG_POSTION = "position";
    ViewPager mVpBigImg;
    TextView mTvImgIndicaor;
    TextView tv_save;
    private int mPosition = 0;
    private ArrayList<String> mImages;

    public static void start(Context context, int position, ArrayList<String> img) {
        Bundle bundle = new Bundle();
        bundle.putInt(BigImageActivity.IMG_POSTION, position);
        bundle.putStringArrayList(BigImageActivity.IMAGES, img);
        Intent intent = new Intent(context, BigImageActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.anim_fade_in, 0);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_big_image;
    }

    @Override
    protected void initVariable() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(IMAGES)) {
                mImages = bundle.getStringArrayList(IMAGES);
            }
            if (bundle.containsKey(IMG_POSTION)) {
                mPosition = bundle.getInt(IMG_POSTION, 0);
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mVpBigImg = findView(R.id.vp_big_img);
        mTvImgIndicaor = findView(R.id.tv_img_indicaor);
        tv_save = findView(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTvImgIndicaor.setText((mPosition + 1) + "/" + mImages.size());
        setAdapter();
    }

    private void setAdapter() {
        ImageAdapter imageAdapter = new ImageAdapter(mImages, this);
        mVpBigImg.setAdapter(imageAdapter);
        if (mPosition < 0)
            mPosition = 0;
        mVpBigImg.setCurrentItem(mPosition);
        imageAdapter.setOnClickListener(new ImageAdapter.onImageLayoutOnClickListener() {
            @Override
            public void OnImageOnClik() {
                finish();
            }
        });
    }


    public void onClick() {
//        Subscription subscribe = RxImage.saveImageAndGetPathObservable(BigImageActivity.this, mImages.get(mVpBigImg.getCurrentItem()))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Uri>() {
//                    @Override
//                    public void call(Uri uri) {
//                        File appDir = new File(Environment.getExternalStorageDirectory(),"App");
//                        String msg = String.format(getString(R.string.picture_has_save_to),
//                                appDir.getAbsolutePath());
//                        ToastUtil.show(msg);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        ToastUtil.show("图片保存失败，请重试");
//                    }
//                });
//        addSubscription(subscribe);
    }

    @Override
    protected void setListener() {
        mVpBigImg.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTvImgIndicaor.setText((position + 1) + "/" + mImages.size());
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
