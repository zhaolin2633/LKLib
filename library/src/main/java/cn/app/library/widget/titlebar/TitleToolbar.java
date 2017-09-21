package cn.app.library.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.app.library.R;

/**
 *
 * <pre>
 * author : zhaolin
 * time : 2017/09/15
 * desc :  //注意：高度一定要用wrap_content属性
 * <cn.app.library.widget.titlebar.TitleToolbar
 * xmlns:app="http://schemas.android.com/apk/res-auto"
 * android:id="@+id/toolbar"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@color/app_color"
 * app:theme="@style/ToolBarColorStyle" />
 * </pre>
 */
public class TitleToolbar extends Toolbar {
    private TextView centerTitleTv;
    private TextView rightTitleTv;

    public TitleToolbar(Context context) {
        super(context);
        init();
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addView(centerTitleTv = createCenterTitle());
        addView(rightTitleTv = createRightTitle());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //默认支持侵浸效果（暂时隐藏，xml布局无法显示）
        //setImmerseState((Activity) getContext());
    }

    public TitleToolbar setNavIcon(int resId) {
        setNavigationIcon(resId);
        return this;
    }

    public TitleToolbar setNavigationListener(OnClickListener onClickListener) {
        setNavigationOnClickListener(onClickListener);
        return this;
    }

    public TitleToolbar setCenterTitleListener(OnClickListener onClickListener) {
        centerTitleTv.setOnClickListener(onClickListener);
        return this;
    }

    public TitleToolbar setRightTitleListener(OnClickListener onClickListener) {
        rightTitleTv.setOnClickListener(onClickListener);
        return this;
    }

    public TitleToolbar setCenterTitle(String title) {
        setCenterTitle(title, 18);
        return this;
    }

    public TitleToolbar setCenterTitle(String title, int textSize) {
        setCenterTitle(title, textSize, R.color.white);
        return this;
    }

    public TitleToolbar setCenterTitle(String title, int textSize, int colorRes) {
        centerTitleTv.setText(TextUtils.isEmpty(title) ? "" : title);
        centerTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        centerTitleTv.setTextColor(getResources().getColor(colorRes));//R.color.frame_black
        centerTitleTv.setGravity(Gravity.CENTER);
        return this;
    }

    public TitleToolbar setCenterTitle(String title, int textSize, String colorStr) {
        centerTitleTv.setText(TextUtils.isEmpty(title) ? "" : title);
        centerTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        centerTitleTv.setTextColor(Color.parseColor(colorStr));//#000000
        centerTitleTv.setGravity(Gravity.CENTER);
        return this;
    }


    public TitleToolbar setRightTitle(String title) {
        setRightTitle(title, 18);
        return this;
    }

    public TitleToolbar setRightTitle(String title, int textSize) {
        setRightTitle(title, textSize, R.color.transparent);
        return this;
    }

    public TitleToolbar setRightTitle(String title, int textSize, int colorRes) {
        //Toolbar自定义视图设置属性必须使用Toolbar.LayoutParams属性对象
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;//layout_gravity
        params.rightMargin = dip2px(getContext(), 10);
        rightTitleTv.setLayoutParams(params);
        rightTitleTv.setText(TextUtils.isEmpty(title) ? "" : title);
        rightTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        rightTitleTv.setTextColor(getResources().getColor(colorRes));//R.color.frame_black
        rightTitleTv.setGravity(Gravity.CENTER);
        return this;
    }

    public TitleToolbar setRightTitle(String title, int textSize, String colorStr) {
        rightTitleTv.setText(TextUtils.isEmpty(title) ? "" : title);
        rightTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        rightTitleTv.setTextColor(Color.parseColor(colorStr));//#000000
        rightTitleTv.setGravity(Gravity.CENTER);
        return this;
    }

    public TitleToolbar setCenterDraw(int resId, String place, int paddingValue) {
        setDrawableAttr(getContext(), centerTitleTv, resId, place, paddingValue);
        centerTitleTv.setGravity(Gravity.CENTER);
        return this;
    }

    //固定宽高属性（一般设置宽高等比的icon）
    public TitleToolbar setRightImage(int resId) {
        setRightImage(resId, false);
        return this;
    }

    //包裹属性（一般设置宽高不等比的icon）isWrap默认false
    public TitleToolbar setRightImage(int resId, boolean isWrap) {
        //Toolbar自定义视图设置属性必须使用Toolbar.LayoutParams属性对象
        int width;
        int height;
        if (isWrap) {
            width = ViewGroup.LayoutParams.WRAP_CONTENT;
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            width = dip2px(getContext(), 25);
            height = width;
        }
        LayoutParams params = new LayoutParams(width, height);
        params.gravity = Gravity.RIGHT;//layout_gravity
        params.rightMargin = dip2px(getContext(), 10);
        rightTitleTv.setLayoutParams(params);
        rightTitleTv.setBackgroundResource(resId);
        return this;
    }

    /**
     * 是否显示右边视图
     *
     * @param isShow
     */
    public TitleToolbar isShowRightView(boolean isShow) {
        rightTitleTv.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    /**
     * 设置Toolbar是否侵浸状态
     *
     * @param activity
     * @param isImmerse 是否侵浸效果
     * @return
     */
    public TitleToolbar setImmerseState(Activity activity, boolean isImmerse) {
        ImmersedStatusbarUtils.initAfterSetContentView(activity, this, isImmerse);
        return this;
    }

    //默认侵浸效果
    public TitleToolbar setImmerseState(Activity activity) {
        ImmersedStatusbarUtils.initAfterSetContentView(activity, this, true);
        return this;
    }

    private TextView createCenterTitle() {
        TextView centerTitleTv = new TextView(getContext());
        centerTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        centerTitleTv.setTextColor(getResources().getColor(R.color.white));

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;//layout_gravity
        centerTitleTv.setLayoutParams(params);

        return centerTitleTv;
    }

    private TextView createRightTitle() {
        TextView rightTitleTv = new TextView(getContext());
        rightTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        rightTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        rightTitleTv.setTextColor(getResources().getColor(R.color.white));

        //Toolbar自定义视图设置属性必须使用Toolbar.LayoutParams属性对象
        LayoutParams params = new LayoutParams(dip2px(getContext(), 25), dip2px(getContext(), 25));
        params.gravity = Gravity.RIGHT;//layout_gravity
        params.rightMargin = dip2px(getContext(), 10);
        rightTitleTv.setLayoutParams(params);

        return rightTitleTv;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static final String LEFT = "left";
    public static final String TOP = "top";
    public static final String RIGHT = "right";
    public static final String BOTTOM = "bottom";

    /**
     * TextView代码设置内容图片 setCompoundDrawables
     *
     * @param context
     * @param view
     * @param resId
     */
    public void setDrawableAttr(Context context, TextView view, int resId, String place, int paddingValue) {
        Drawable drawable = null;
        if (resId > 0) {
            drawable = context.getResources().getDrawable(resId);

            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
        }

        //设置drawable内容图片的padding值
        view.setCompoundDrawablePadding(paddingValue);

        if (LEFT.equals(place))
            view.setCompoundDrawables(drawable, null, null, null);
        if (TOP.equals(place))
            view.setCompoundDrawables(null, drawable, null, null);
        if (RIGHT.equals(place))
            view.setCompoundDrawables(null, null, drawable, null);
        if (BOTTOM.equals(place))
            view.setCompoundDrawables(null, null, null, drawable);
    }
}
