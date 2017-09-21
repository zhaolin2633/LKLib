package cn.app.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import cn.app.library.R;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.utils.SoftKeyBoardUtils;

/**
 * @link https://xiaohaibin.github.io/
 * @email： xhb_199409@163.com
 * @github: https://github.com/xiaohaibin
 * @description： 单Activity多Fragment的容器Activity
 */
public class ContainerFragmentActivity extends BaseFragmentActivity {

    //导航条
    private RelativeLayout navigationBar;
    //导航条默认文字标题
    private TextView navigationBarTitle;
    //导航条返回按钮
    private ImageView backButton;
    //导航条按钮
    private LinearLayout actionLayout;

    private String className = "";
    private String title = "";
    private Bundle arguments;
    private Fragment fragment;

    private OnKeyDownListener onKeyDownListener;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (getIntent().getBooleanExtra("fullScreen", false)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_container);
        init();
        findViews();
        setStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void init() {
        Intent intent = getIntent();
        if (intent.hasExtra("className")) {
            className = intent.getStringExtra("className");
        }
        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title");
        }
        if (intent.hasExtra("arguments")) {
            arguments = intent.getBundleExtra("arguments");
        }
        if (!TextUtils.isEmpty(className)) {
            try {
                fragment = (Fragment) Class.forName(className).newInstance();
                if (arguments != null) {
                    fragment.setArguments(arguments);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void findViews() {

        //导航条
        navigationBar = (RelativeLayout) findViewById(R.id.container_navigation_bar);
        //导航条背景
        navigationBarTitle = (TextView) findViewById(R.id.container_navigation_bar_title);
        //导航条返回按钮
        backButton = (ImageView) findViewById(R.id.container_navigation_bar_back);
        //导航条按钮
        actionLayout = (LinearLayout) findViewById(R.id.container_navigation_bar_action);

        initViews();
        registerViews();
    }

    @Override
    protected void initViews() {
        navigationBar.setBackgroundColor(getResources().getColor(R.color.common_navigation));

        navigationBarTitle.setText(title);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();
        }
    }

    @Override
    protected void registerViews() {
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                SoftKeyBoardUtils.closeSoftInput(ContainerFragmentActivity.this);
            }
        });
    }

    public void addImageButton(int imageResId, String tag, OnClickListener onClickListener) {
        ImageView imageView = new ImageView(this);
        LayoutParams params = new LayoutParams(ScreenUtil.dip2px(this, 48), LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setTag(tag);
        imageView.setPadding(ScreenUtil.dip2px(this, 12), ScreenUtil.dip2px(this, 12), ScreenUtil.dip2px(this, 12), ScreenUtil.dip2px(this, 12));
        imageView.setBackgroundResource(R.drawable.selector_trans_divider);
        imageView.setImageResource(imageResId);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        imageView.setOnClickListener(onClickListener);
        actionLayout.addView(imageView);
    }

    public void addImageButton(int imageResId, String tag, OnClickListener onClickListener, int padding) {
        ImageView imageView = new ImageView(this);
        LayoutParams params = new LayoutParams(ScreenUtil.dip2px(this, 48), LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setTag(tag);
        imageView.setPadding(ScreenUtil.dip2px(this, padding), ScreenUtil.dip2px(this, padding), ScreenUtil.dip2px(this, padding), ScreenUtil.dip2px(this, padding));
        imageView.setBackgroundResource(R.drawable.selector_trans_divider);
        imageView.setImageResource(imageResId);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        imageView.setOnClickListener(onClickListener);
        actionLayout.addView(imageView);
    }

    public void addTextButton(String text, OnClickListener onClickListener) {
        addTextButton(text, getResources().getColor(R.color.common_title_sub), onClickListener);
    }

    public void addTextButton(String text, int textColor, OnClickListener onClickListener) {
        TextView textView = new TextView(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(ScreenUtil.dip2px(this, 16), 0, ScreenUtil.dip2px(this, 16), 0);
        textView.setMinWidth(ScreenUtil.dip2px(this, 64));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.selector_trans_divider);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setOnClickListener(onClickListener);
        actionLayout.addView(textView);
    }

    public void hideNavigationBar() {
        navigationBar.setVisibility(View.GONE);
    }

    public String getTitleText() {
        return title;
    }

    public ImageView getBackButton() {
        return backButton;
    }

    public void clearActionButton() {
        actionLayout.removeAllViews();
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener) {
        this.onKeyDownListener = onKeyDownListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onKeyDownListener != null) {
            if (onKeyDownListener.onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }


    public LinearLayout getActionLayout() {
        return actionLayout;
    }

    public void setNavigationBarTitle(String title) {
        if (navigationBarTitle != null&&!TextUtils.isEmpty(title)) {
            this.title = title;
            navigationBarTitle.setText(title);
        }
    }


}
