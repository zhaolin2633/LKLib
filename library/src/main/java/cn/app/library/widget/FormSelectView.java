package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.app.library.R;
import cn.app.library.utils.ScreenUtil;

/**
 * Description:表单选择控件
 */
public class FormSelectView extends LinearLayout {

    private TextView nameTextView;
    private TextView valueTextView;
    private ImageView imageView;

    private String name;
    private int nameWidth;
    private float nameTextSize;
    private int nameTextColor;
    private int namePadding;

    private String valueHint;
    private float valueTextSize;
    private int valueTextColor;
    private int valueTextColorHint;

    private int indicatorImgRes;
    private boolean mValueIsRight;

    public FormSelectView(Context context) {
        super(context);
        init(null);
    }

    public FormSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FormSelectView);
            name = typedArray.getString(R.styleable.FormSelectView_FormSelectView_name);
            nameWidth = typedArray.getDimensionPixelSize(R.styleable.FormSelectView_FormSelectView_nameWidth, ScreenUtil.dip2px(getContext(), 100));
            nameTextSize = typedArray.getDimensionPixelSize(R.styleable.FormSelectView_FormSelectView_nameTextSize, ScreenUtil.dip2px(getContext(), 15));
            nameTextColor = typedArray.getColor(R.styleable.FormSelectView_FormSelectView_nameTextColor, Color.parseColor("#111111"));
            namePadding = typedArray.getDimensionPixelSize(R.styleable.FormSelectView_FormSelectView_namePadding, ScreenUtil.dip2px(getContext(), 12));

            valueHint = typedArray.getString(R.styleable.FormSelectView_FormSelectView_valueHint);
            valueTextSize = typedArray.getDimensionPixelSize(R.styleable.FormSelectView_FormSelectView_valueTextSize, ScreenUtil.dip2px(getContext(), 15));
            valueTextColor = typedArray.getColor(R.styleable.FormSelectView_FormSelectView_valueTextColor, Color.parseColor("#111111"));
            valueTextColorHint = typedArray.getColor(R.styleable.FormSelectView_FormSelectView_valueTextColorHint, Color.parseColor("#cccccc"));
            mValueIsRight = typedArray.getBoolean(R.styleable.FormSelectView_FormSelectView_valueIsRight, false);
            indicatorImgRes = typedArray.getResourceId(R.styleable.FormSelectView_FormSelectView_indicatorImgRes, R.mipmap.loading_default);
            typedArray.recycle();
        } else {
            name = "";
            nameWidth = ScreenUtil.dip2px(getContext(), 100);
            nameTextSize = ScreenUtil.dip2px(getContext(), 15);
            nameTextColor = Color.parseColor("#111111");
            namePadding = ScreenUtil.dip2px(getContext(), 12);

            valueHint = "";
            valueTextSize = ScreenUtil.dip2px(getContext(), 15);
            valueTextColor = Color.parseColor("#111111");
            valueTextColorHint = Color.parseColor("#cccccc");

            indicatorImgRes = R.mipmap.loading_default;
        }
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        nameTextView = new TextView(getContext());
        nameTextView.setLayoutParams(new LayoutParams(nameWidth, LayoutParams.WRAP_CONTENT));
        nameTextView.setSingleLine(true);
        nameTextView.setPadding(namePadding, 0, namePadding, 0);
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameTextSize);
        nameTextView.setTextColor(nameTextColor);
        nameTextView.setText(name);
        addView(nameTextView);

        valueTextView = new TextView(getContext());
        LayoutParams valueParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        valueParams.weight = 1;
        valueTextView.setLayoutParams(valueParams);
        valueTextView.setGravity(mValueIsRight?Gravity.RIGHT|Gravity.CENTER_VERTICAL:Gravity.CENTER_VERTICAL);
        valueTextView.setSingleLine(true);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
        valueTextView.setTextColor(valueTextColor);
        valueTextView.setEllipsize(TextUtils.TruncateAt.END);
        valueTextView.setHintTextColor(valueTextColorHint);
        valueTextView.setHint(valueHint);
        valueTextView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        addView(valueTextView);

        imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(ScreenUtil.dip2px(getContext(), 40), ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(ScreenUtil.dip2px(getContext(), 12), 0, ScreenUtil.dip2px(getContext(), 12), 0);
        imageView.setImageResource(indicatorImgRes);
        addView(imageView);
    }

    public String getValue() {
        return valueTextView.getText().toString();
    }

    public void setValue(String value) {
        valueTextView.setText(value);
    }

    public TextView getValueTextView() {
        return valueTextView;
    }

    public void hideArrow() {
        imageView.setVisibility(GONE);
    }

    public void showArrow() {
        imageView.setVisibility(VISIBLE);
    }

}
