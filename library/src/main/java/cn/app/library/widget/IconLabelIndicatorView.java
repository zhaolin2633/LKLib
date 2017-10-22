package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.app.library.R;


/**
 * @description： 自定义组合控件实现 个人中心常见标签项效果（有icon图标）
 */
public class IconLabelIndicatorView extends ViewGroup {

    private View contentView;
    private ImageView iconImageView;
    private TextView labelTextView;
    private ImageView indicatorImageView;
    private TextView tvTip;

    public IconLabelIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public IconLabelIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        contentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    private void init(AttributeSet attrs) {
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_widget_icon_label_indicator_view, null);
        iconImageView = (ImageView) contentView.findViewById(R.id.icon);
        labelTextView = (TextView) contentView.findViewById(R.id.label);
        indicatorImageView = (ImageView) contentView.findViewById(R.id.indicator);
        tvTip = (TextView) contentView.findViewById(R.id.tv_tip);
        addView(contentView);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IconLabelIndicatorView);
            iconImageView.setImageResource(typedArray.getResourceId(R.styleable.IconLabelIndicatorView_viewIcon, 0));
            labelTextView.setText(typedArray.getString(R.styleable.IconLabelIndicatorView_viewLabel));
            tvTip.setText(typedArray.getString(R.styleable.IconLabelIndicatorView_viewTip));
            tvTip.setTextColor(typedArray.getColor(R.styleable.IconLabelIndicatorView_viewTipColor, getResources().getColor(R.color.mask_color)));
            indicatorImageView.setImageResource(typedArray.getResourceId(R.styleable.IconLabelIndicatorView_viewIndicator, 0));
            typedArray.recycle();
        }
    }

    public void setTipText(String text) {
        if (tvTip != null) {
            tvTip.setText(text);
        }
    }

    public void setTipColor(@ColorRes int color) {
        if (tvTip != null) {
            tvTip.setTextColor(getResources().getColor(color));
        }
    }

    public String getTipText() {
        if (tvTip != null) {
            return tvTip.getText().toString();
        }
        return "";
    }

    public void setLabelText(String text) {
        if (labelTextView != null) {
            labelTextView.setText(text);
        }
    }

    public void setLabelColor(@ColorRes int color) {
        if (labelTextView != null) {
            labelTextView.setTextColor(getResources().getColor(color));
        }
    }

    public void setIndicatorImage(int resId) {
        if (indicatorImageView != null) {
            indicatorImageView.setImageResource(resId);
        }
    }

    public void setIconImage(int resId) {
        if (iconImageView != null) {
            iconImageView.setImageResource(resId);
        }
    }

}
