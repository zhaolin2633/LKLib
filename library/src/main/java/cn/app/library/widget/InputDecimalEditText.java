package cn.app.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import cn.app.library.R;

/**
 * Describe：处理输入小数点这些的输入框
 */
@SuppressLint("AppCompatCustomView")
public class InputDecimalEditText extends EditText implements TextWatcher {

    private static final int DECIMAL_DIGITS = 2;//小数的默认位数
    private TextChangedListener mTextChangedListener;
    private int decimal_length;

    public InputDecimalEditText(Context context) {
        super(context);
        init(null);
    }

    public InputDecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        decimal_length = DECIMAL_DIGITS;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputDecimalEditText);
            decimal_length = typedArray.getInteger(R.styleable.InputDecimalEditText_decimal_length, DECIMAL_DIGITS);
            typedArray.recycle();
        }
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > decimal_length) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + decimal_length + 1);
                setText(s);
                setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            setText(s);
            setSelection(2);
        }
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                setText(s.subSequence(0, 1));
                setSelection(1);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTextChangedListener != null) {
            mTextChangedListener.onTextChanged();
        }
    }

    public int getDecimal_length() {
        return decimal_length;
    }

    public void setTextChangedListener(TextChangedListener textChangedListener) {
        mTextChangedListener = textChangedListener;
    }

    public interface TextChangedListener {
        void onTextChanged();
    }
}
