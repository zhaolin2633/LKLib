package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.app.library.R;


/**
 * @description： 封装好的通用标题栏控件
 */
public class CustomTitlebar extends RelativeLayout {

    /**
     * 标题栏的根布局
     */
    private RelativeLayout mRelativeLayout;
    /**
     * 标题栏的左边按返回图标
     */
    private ImageView mIvLeft;
    /**
     * 标题栏的右边图标
     */
    private ImageView mIvRight;
    /**
     * 标题栏左边按钮文字
     */
    private TextView mTvLeft;

    /**
     * 右边按钮的文字颜色
     */
    private int left_button_textColor;
    /**
     * 右边保存按钮的文字大小
     */
    private int left_button_textSize;
    /**
     * 标题栏文字标题
     */
    private TextView mTvTilte;
    /**
     * 标题栏文字标题后面的字样
     */
    private TextView mTvTilteCount;

    /**
     * 标题栏的背景颜色
     */
    private int title_background_color;
    private Drawable title_background_reference;

    /**
     * 标题栏的显示的标题文字
     */
    private String title_text;

    /**
     * 标题栏的显示的标题文字
     */
    private String title_text_count;
    /**
     * 标题栏的显示的标题文字颜色
     */
    private int title_textColor;
    /**
     * 标题栏的显示的标题文字大小
     */
    private int title_textSize;
    private boolean lineVisibility;

    /**
     * 标题栏的顶部分割线
     */
    private View line;
    /**
     * 标题栏的右边按钮文字
     */
    private TextView mTvRight;
    /**
     * 右边保存按钮的资源图片
     */
    private int right_button_image_id;
    /**
     * 右边保存按钮的文字
     */
    private String right_button_text;
    /**
     * 右边按钮的文字颜色
     */
    private int right_button_textColor;
    /**
     * 右边保存按钮的文字大小
     */
    private int right_button_textSize;

    /**
     * 返回按钮上显示的文字
     */
    private String left_button_text;
    /**
     * 返回按钮的资源图片
     */
    private int left_button_imageId;


    private TitleBarOnClickListener mTitleBarOnClickListener;
    private LinearLayout mLlLeft;
    private LinearLayout mllRightContainer;

    public CustomTitlebar(Context context) {
        this(context, null);
    }

    public CustomTitlebar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setListener();
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        /**加载布局文件*/
        View.inflate(context, R.layout.actionbar, this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relay_background);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mTvLeft = (TextView) findViewById(R.id.tv_left);
        mTvTilte = (TextView) findViewById(R.id.tv_title);
        mTvTilteCount = (TextView) findViewById(R.id.tv_title_count);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
        mLlLeft = (LinearLayout) findViewById(R.id.ll_left);
        mllRightContainer = (LinearLayout) findViewById(R.id.ll_right_container);
        line = findViewById(R.id.line);
    }


    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitlebar);
        /**返回按钮相关*/
        left_button_imageId = typedArray.getResourceId(R.styleable.CustomTitlebar_left_button_image, 0);
        left_button_text = typedArray.getString(R.styleable.CustomTitlebar_left_button_text);
        left_button_textColor = typedArray.getColor(R.styleable.CustomTitlebar_left_button_textColor, Color.GRAY);
        left_button_textSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitlebar_left_button_textSize, sp2px(context, 14));

        /**标题相关*/
        title_background_color = typedArray.getColor(R.styleable.CustomTitlebar_title_background, Color.WHITE);
        title_background_reference = typedArray.getDrawable(R.styleable.CustomTitlebar_title_background_reference);
        title_text = typedArray.getString(R.styleable.CustomTitlebar_title_text);
        title_textColor = typedArray.getColor(R.styleable.CustomTitlebar_title_textColor, Color.GRAY);
        title_textSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitlebar_title_textSize, sp2px(context, 14));

        /**右边保存按钮相关*/
        right_button_image_id = typedArray.getResourceId(R.styleable.CustomTitlebar_right_button_image, 0);
        right_button_text = typedArray.getString(R.styleable.CustomTitlebar_right_button_text);
        right_button_textColor = typedArray.getColor(R.styleable.CustomTitlebar_right_button_textColor, Color.GRAY);
        right_button_textSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitlebar_right_button_textSize, sp2px(context, 14));

        /**分割线*/
        lineVisibility = typedArray.getBoolean(R.styleable.CustomTitlebar_show_line, false);

        /**设置值*/
        if (title_background_reference != null) {
            setTitleBackgroundResource(title_background_reference);
        } else
            setTitleBarBackground(title_background_color);
        setTilte(title_text);
        setTitleTextSize(title_textSize);
        setTitle_textColor(title_textColor);

        setLeftIcon(left_button_imageId);
        setTvLeft(left_button_text);
        setTvLeftTextSize(left_button_textSize);
        setTvLeftTextColor(left_button_textColor);

        setRightIcon(right_button_image_id);
        setTvRight(right_button_text);
        setTvRightTextColor(right_button_textColor);
        setTvRightTextSize(right_button_textSize);
        setLineIsVisible(lineVisibility);
        typedArray.recycle();
    }

    public void setTilteLayoutParams(int gravity, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = gravity;
        if (weight > 0) {
            params.weight = weight;
        }
        mTvTilte.setLayoutParams(params);
    }

    public void setTilte(String tilte) {
        if (TextUtils.isEmpty(tilte)) {
            mTvTilte.setVisibility(GONE);
        } else {
            mTvTilte.setText(tilte);
            mTvTilte.setVisibility(VISIBLE);
        }
    }

    public void setTilte(int resId) {
        if (resId == 0) {
            mTvTilte.setVisibility(GONE);
        } else {
            mTvTilte.setText(resId);
            mTvTilte.setVisibility(VISIBLE);
        }
    }

    public void setTilteCount(String tilteCount) {
        if (TextUtils.isEmpty(tilteCount)) {
            mTvTilteCount.setVisibility(GONE);
        } else {
            mTvTilteCount.setText(tilteCount);
            mTvTilteCount.setVisibility(VISIBLE);
        }
    }


    public void setTitleTextSize(int textSize) {
        mTvTilte.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setTitle_textColor(int textColor) {
        mTvTilte.setTextColor(textColor);
    }

    public void setTvLeft(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvLeft.setVisibility(GONE);
        } else {
            mTvLeft.setVisibility(VISIBLE);
            mTvLeft.setText(text);
        }
    }

    public void setTvLeftTextSize(int textSize) {
        mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setTvLeftTextColor(int textColor) {
        mTvLeft.setTextColor(textColor);
    }

    public void setTvRight(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvRight.setVisibility(GONE);
        } else {
            mTvRight.setVisibility(VISIBLE);
            mTvRight.setText(text);
        }
    }

    /**
     * 设置右侧文字大小
     *
     * @param textSize
     */
    public void setTvRightTextSize(int textSize) {
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * 设置右侧文字颜色
     *
     * @param textColor
     */
    public void setTvRightTextColor(int textColor) {
        mTvRight.setTextColor(textColor);
    }

    /**
     * 设置左边按钮图片资源
     *
     * @param resId
     */
    public void setLeftIcon(@DrawableRes int resId) {
        if (resId == 0) {
            mIvLeft.setVisibility(View.GONE);
        } else {
            mIvLeft.setVisibility(View.VISIBLE);
            mIvLeft.setImageResource(resId);
        }
    }

    /**
     * 设置右边按钮图片资源
     *
     * @param resId
     */
    public void setRightIcon(@DrawableRes int resId) {
        if (resId == 0) {
            mIvRight.setVisibility(View.GONE);
        } else {
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageResource(resId);
        }
    }

    private void setListener() {
        mLlLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleBarOnClickListener != null) {
                    mTitleBarOnClickListener.onClickTitleLeft();
                }
            }
        });
        mllRightContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleBarOnClickListener != null) {
                    mTitleBarOnClickListener.onClickTitleRight();
                }
            }
        });
        mTvTilte.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleBarOnClickListener != null) {
                    mTitleBarOnClickListener.onClickTitle();
                }
            }
        });
    }

    /**
     * 设置是否显示分割线
     *
     * @param visibility
     */
    public void setLineIsVisible(boolean visibility) {
        line.setVisibility(visibility ? VISIBLE : GONE);
    }

    /**
     * 设置是否显示右边按钮
     *
     * @param show_right_button
     */
    public void setShow_right_button(boolean show_right_button) {
        mTvRight.setVisibility(show_right_button ? VISIBLE : INVISIBLE);
        mIvRight.setVisibility(show_right_button ? VISIBLE : INVISIBLE);
    }

    public void setShowTvRightBtn(boolean isShow) {
        mTvRight.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public void setShowIvRightBtn(boolean isShow) {
        mIvRight.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }


    /**
     * 设置是否显示左边按钮
     *
     * @param show_left_button
     */
    public void setShow_left_button(boolean show_left_button) {
        mTvLeft.setVisibility(show_left_button ? VISIBLE : INVISIBLE);
        mIvLeft.setVisibility(show_left_button ? VISIBLE : INVISIBLE);
    }

    /**
     * 设置标题栏背景色
     *
     * @param resId
     */
    public void setTitleBarBackground(int resId) {
        mRelativeLayout.setBackgroundColor(resId);
    }

    /**
     * 设置标题栏背景色
     *
     * @param resId
     */
    public void setTitleBackgroundResource(Drawable resId) {
        mRelativeLayout.setBackgroundDrawable(resId);
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public void setTitleBarOnClickListener(TitleBarOnClickListener titleBarOnClickListener) {
        mTitleBarOnClickListener = titleBarOnClickListener;
    }

    public interface TitleBarOnClickListener {

        void onClickTitleLeft();

        void onClickTitle();

        void onClickTitleRight();

    }

    public static class SimpleOnClickListener implements TitleBarOnClickListener {
        @Override
        public void onClickTitleLeft() {

        }

        @Override
        public void onClickTitle() {

        }

        @Override
        public void onClickTitleRight() {

        }
    }

    /**
     * 设置自定义按钮
     *
     * @param resId
     * @param listener
     */
    public void addCustomizationItem(int resId, View.OnClickListener listener) {

        LinearLayout view = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(R.layout.toolbar_item, null);
        ((ImageView) view.findViewById(R.id.img)).setImageResource(resId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mllRightContainer.addView(view, params);
        view.setOnClickListener(listener);

    }

    public ImageView getIvRight() {
        return mIvRight;
    }

    public String getTvRight() {
        return mTvRight.getText().toString();
    }
}
