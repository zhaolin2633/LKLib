package cn.app.library.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import cn.app.library.R;
import cn.app.library.base.BaseAppCompatActivity;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * @describe: 引导页
 */
public abstract class GuideActivity extends BaseAppCompatActivity implements View.OnClickListener {

    protected BGABanner bgaBanner;
    protected TextView btnEnter;
    private static final String TAG = "GuideActivity";

    @Override
    protected int getContentView() {
        return R.layout.activity_guide;
    }

    /**
     * 去主页
     */
    public abstract void toMainActivity();

    public abstract ArrayList getArraysImg();

    public abstract void setBtnEnterAttr();

    @Override
    protected void initView() {
        bgaBanner = (BGABanner) findViewById(R.id.banner_guide_foreground);
        btnEnter = (TextView) findViewById(R.id.btn_enter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setBtnEnterAttr();
    }

    @Override
    protected void initData() {
        bgaBanner.setData(R.layout.bga_banner_item_image, getArraysImg(), null);
        bgaBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                ((ImageView) view).setImageResource((int) model);
            }
        });
    }

    @Override
    protected void setListener() {
        btnEnter.setOnClickListener(this);
        bgaBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == bgaBanner.getItemCount() - 2) {
                    if (positionOffset > 0.5f) {
                        btnEnter.setVisibility(View.VISIBLE);
                    } else {
                        btnEnter.setVisibility(View.GONE);
                    }
                } else if (position == bgaBanner.getItemCount() - 1) {
                    btnEnter.setVisibility(View.VISIBLE);
                } else {
                    btnEnter.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        toMainActivity();
        finish();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
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
