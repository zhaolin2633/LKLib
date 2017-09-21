package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.app.library.R;
import cn.app.library.widget.toast.ToastCustomUtils;

/**
 * Author：xiaohaibin
 * Time：2017/8/7
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：具有输入限制的输入框
 */
public class InputLimitEditText extends LinearLayout {

    private EditText mEdContent;
    private TextView mTvCount;
    private static final int INPUT_MAX_LENGTH = 500;//最大输入长度
    private int maxLength = INPUT_MAX_LENGTH;

    public InputLimitEditText(Context context) {
        super(context);
        init(null);
    }

    public InputLimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_input_limit_edit_text, this);
        mEdContent = (EditText) findViewById(R.id.ed_content);
        mTvCount = (TextView) findViewById(R.id.tv_count);
        setListener();
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputLimitEditText);
            mEdContent.setHint(typedArray.getText(R.styleable.InputLimitEditText_input_limit_hint));
            maxLength = typedArray.getInteger(R.styleable.InputLimitEditText_input_limit_max_length, INPUT_MAX_LENGTH);
            mTvCount.setText("0/" + maxLength);
            typedArray.recycle();
        }
    }

    private void setListener() {
        if (mEdContent != null && mTvCount != null) {
            mEdContent.addTextChangedListener(new TextWatcher() {
                private CharSequence temp;
                private int selectionStart;
                private int selectionEnd;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    temp = s;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mTvCount.setText(s.length() + "/" + maxLength);
                    selectionStart = mEdContent.getSelectionStart();
                    selectionEnd = mEdContent.getSelectionEnd();
                    if (temp.length() > maxLength) {
                        s.delete(selectionStart - 1, selectionEnd);
                        int tempSelection = selectionEnd;
                        mEdContent.setText(s);
                        mEdContent.setSelection(tempSelection);
                        ToastCustomUtils.showShort(getContext(), "最多只能输入" + maxLength + "字");
                    }
                }
            });
        }
    }

    /**
     * 获取输入框内容
     *
     * @return
     */
    public String getInputContent() {
        if (mEdContent != null)
            return mEdContent.getText().toString();
        return "";
    }

    /**
     * 获取输入框内容长度
     *
     * @return
     */
    public int getInputContentLength() {
        if (mEdContent != null)
            return mEdContent.getText().toString().length();
        return 0;
    }

}
