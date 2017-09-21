package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.app.library.R;
import cn.app.library.utils.ScreenUtil;

/**
 * Description:表单输入控件
 */
public class FormEditView extends LinearLayout {

    private TextView nameTextView;
    private EditText valueEditText;
    private TextWatcher textWatcher;

    public FormEditView(Context context) {
        super(context);
        init(null);
    }

    public FormEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        String name;
        int nameWidth;
        float nameTextSize;
        int nameTextColor;
        int namePadding;

        String valueHint;
        float valueTextSize;
        int valueTextColor;
        int valueTextColorHint;
        int valuePaddingRight;
        int valueMaxLength;
        int valueInputType;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FormEditView);
            name = typedArray.getString(R.styleable.FormEditView_FormEditView_name);
            nameWidth = typedArray.getDimensionPixelSize(R.styleable.FormEditView_FormEditView_nameWidth, ScreenUtil.dip2px(getContext(), 100));
            nameTextSize = typedArray.getDimensionPixelSize(R.styleable.FormEditView_FormEditView_nameTextSize, ScreenUtil.dip2px(getContext(), 15));
            nameTextColor = typedArray.getColor(R.styleable.FormEditView_FormEditView_nameTextColor, Color.parseColor("#111111"));
            namePadding = typedArray.getDimensionPixelSize(R.styleable.FormEditView_FormEditView_namePadding, ScreenUtil.dip2px(getContext(), 12));

            valueHint = typedArray.getString(R.styleable.FormEditView_FormEditView_valueHint);
            valueTextSize = typedArray.getDimensionPixelSize(R.styleable.FormEditView_FormEditView_valueTextSize, ScreenUtil.dip2px(getContext(), 15));
            valueTextColor = typedArray.getColor(R.styleable.FormEditView_FormEditView_valueTextColor, Color.parseColor("#111111"));
            valueTextColorHint = typedArray.getColor(R.styleable.FormEditView_FormEditView_valueTextColorHint, Color.parseColor("#cccccc"));
            valuePaddingRight = typedArray.getDimensionPixelSize(R.styleable.FormEditView_FormEditView_valuePaddingRight, ScreenUtil.dip2px(getContext(), 12));
            valueMaxLength = typedArray.getInt(R.styleable.FormEditView_FormEditView_valueMaxLength, 0);
            valueInputType = typedArray.getInt(R.styleable.FormEditView_FormEditView_valueInputType, 0);
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
            valuePaddingRight = ScreenUtil.dip2px(getContext(), 12);
            valueMaxLength = 0;
            valueInputType = 0;
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

        valueEditText = new EditText(getContext());
        LayoutParams valueParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        valueParams.weight = 1;
        valueEditText.setLayoutParams(valueParams);
        valueEditText.setGravity(Gravity.CENTER_VERTICAL);
        valueEditText.setSingleLine(true);
        valueEditText.setPadding(0, 0, valuePaddingRight, 0);
        valueEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
        valueEditText.setTextColor(valueTextColor);
        valueEditText.setHintTextColor(valueTextColorHint);
        valueEditText.setHint(valueHint);
        valueEditText.setBackgroundColor(Color.argb(0, 0, 0, 0));

        switch (valueInputType) {
            case 1:
                //phone
                valueMaxLength = 11;
                valueEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (textWatcher != null) {
                            textWatcher.beforeTextChanged(charSequence, i, i1, i2);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (textWatcher != null) {
                            textWatcher.onTextChanged(charSequence, i, i1, i2);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (valueEditText.length() > 0) {
                            if (!valueEditText.getText().toString().startsWith("1")) {
                                valueEditText.setText("");
                                return;
                            }
                        }
                        if (textWatcher != null) {
                            textWatcher.afterTextChanged(editable);
                        }
                    }
                });
                break;
            case 2:
                //number
                valueEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                break;
            case 3:
                //password
                valueEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                valueEditText.setKeyListener(new DigitsKeyListener() {
                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    }

                    @Override
                    protected char[] getAcceptedChars() {
                        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
                    }
                });
                break;
            case 4:
                //decimal
                valueEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                break;
            case 5:
                //idCard
                valueEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789Xx"));
                break;
            case 6:
                //telephone
                valueMaxLength = 11;
                valueEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                valueEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (textWatcher != null) {
                            textWatcher.beforeTextChanged(charSequence, i, i1, i2);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (textWatcher != null) {
                            textWatcher.onTextChanged(charSequence, i, i1, i2);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (valueEditText.length() > 0) {
                            if (!(valueEditText.getText().toString().startsWith("0") || valueEditText.getText().toString().startsWith("1"))) {
                                valueEditText.setText("");
                                return;
                            }
                        }
                        if (textWatcher != null) {
                            textWatcher.afterTextChanged(editable);
                        }
                    }
                });
                break;
        }
        if (valueMaxLength > 0) {
            valueEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(valueMaxLength)});
        }
        addView(valueEditText);
    }

    public String getValue() {
        return valueEditText.getText().toString();
    }

    public void setValue(String value) {
        valueEditText.setText(value);
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public EditText getValueEditText() {
        return valueEditText;
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public void setEditEnable(boolean isEnable) {
        valueEditText.setEnabled(isEnable);
    }

}
