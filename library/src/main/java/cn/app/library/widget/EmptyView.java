package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.app.library.R;
import cn.app.library.utils.ScreenUtil;

/**
 * Description:默认空数据提示
 */
public class EmptyView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    private int image;
    private String label;

    private OnRefreshClickListener mRefreshClickListener;

    public EmptyView(Context context) {
        super(context);
        init(null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyView);
            image = typedArray.getResourceId(R.styleable.EmptyView_EmptyView_image, 0);
            label = typedArray.getString(R.styleable.EmptyView_EmptyView_label);
            typedArray.recycle();
        }
        final int margins = ScreenUtil.dip2px(getContext(), 0);
        imageView = new ImageView(getContext());
        LayoutParams imageLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLayoutParams.setMargins(margins, margins, margins, margins);
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(image);
        addView(imageView);

        textView = new TextView(getContext());
        LayoutParams textLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(margins, margins, margins, margins);
        textView.setLayoutParams(textLayoutParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setText(label);
        textView.setTextColor(Color.parseColor("#555555"));
        addView(textView);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRefreshClickListener != null) {
                    mRefreshClickListener.OnRefresh();
                }
            }
        });
    }

    public void setImage(int image) {
        this.image = image;
        if (image == 0) {
            imageView.setVisibility(GONE);
        } else {
            imageView.setVisibility(VISIBLE);
            imageView.setImageResource(image);
        }
    }

    public void setLabel(String label) {
        this.label = label;
        if (TextUtils.isEmpty(label)) {
            textView.setVisibility(GONE);
        } else {
            textView.setVisibility(VISIBLE);
            textView.setText(label);
        }
    }

    public void setRefreshClickListener(OnRefreshClickListener refreshClickListener) {
        mRefreshClickListener = refreshClickListener;
    }

    public interface OnRefreshClickListener {
        void OnRefresh();
    }
}
