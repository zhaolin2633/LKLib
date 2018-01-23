package cn.app.library.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import cn.app.library.R;
import cn.app.library.base.BaseDialogFragment;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.widget.toast.ToastUtil;


/**
 * Created by zhaolin on 2017/5/25.
 * 文字输入弹出框
 */

public class TextInputContentDialogFragment extends BaseDialogFragment {
    public LinearLayout ll_layout;
    public ClearEditText ed_input;
    public TextView tv_title;
    public TextView tv_cancel;
    public TextView tv_ok;
    public TextType text;

    public static TextInputContentDialogFragment newInstance(TextType text) {
        Bundle args = new Bundle();
        args.putSerializable("data", text);
        TextInputContentDialogFragment fragment = new TextInputContentDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setInputEditBg(int resId) {
        if (ed_input != null) {
            ed_input.setBackgroundResource(resId);
        }
    }

    public void setInputEditBgColor(int colorId) {
        if (ed_input != null) {
            ed_input.setBackgroundColor(ContextCompat.getColor(getContext(), colorId));
        }
    }

    public void setLayoutBg(int resId) {
        if (ll_layout != null) {
            ll_layout.setBackgroundResource(resId);
        }
    }

    public void setLayoutBgColor(int colorId) {
        if (ll_layout != null) {
            ll_layout.setBackgroundColor(ContextCompat.getColor(getContext(), colorId));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_content);
        init();
    }

    public void setOnItemClickLisnner(OnItemClickLisnner onItemClickLisnner) {
        this.onItemClickLisnner = onItemClickLisnner;
    }

    OnItemClickLisnner onItemClickLisnner;

    public interface OnItemClickLisnner {
        public void onItemClick(View v, TextType bean);

        public void onCancelClick(View v, TextType bean);
    }

    @Override
    protected void initViews() {
        super.initViews();
        text = (TextType) getArguments().getSerializable("data");
        ll_layout = findView(R.id.ll_layout);
        tv_title = findView(R.id.tv_title);
        ed_input = findView(R.id.ed_input);
        tv_cancel = findView(R.id.tv_cancel);
        tv_ok = findView(R.id.tv_ok);
        tv_title.setText(text != null && !TextUtils.isEmpty(text.title) ? text.title : "提示");
        tv_cancel.setText(text != null && !TextUtils.isEmpty(text.leftText) ? text.leftText : "取消");
        if (text != null) {
            tv_cancel.setVisibility(text.isHindLeftBtn ? View.VISIBLE : View.GONE);
            tv_ok.setVisibility(text.isHindRghitBtn ? View.VISIBLE : View.GONE);
        }
        tv_ok.setText(text != null && !TextUtils.isEmpty(text.rghitText) ? text.rghitText : "完成");

        ed_input.setHint(text != null && !TextUtils.isEmpty(text.inputHintText) ? text.inputHintText : "请输入内容");
        if (text != null && !TextUtils.isEmpty(text.inputText)) {
            ed_input.setSelection(text.inputText.length());
            ed_input.setText(text.inputText);
            if (text.inputBg > 0) {
                ed_input.setBackgroundResource(text.inputBg);
            }
            if (text.inputTextLeng > 0) {
                ed_input.setMaxEms(text.inputTextLeng);
            }
        }
        ed_input.setInputType(text != null && text.inputType > 0 ? text.inputType : InputType.TYPE_CLASS_TEXT);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text == null)
                    text = new TextType();
                text.inputText = ed_input.getText().toString().trim();
                if (onItemClickLisnner != null)
                    onItemClickLisnner.onCancelClick(v, text);
                dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_input.getText().toString().trim())) {
                    ToastUtil.show("请输入内容");
                    return;
                }
                if (text == null)
                    text = new TextType();
                text.inputText = ed_input.getText().toString().trim();
                if (onItemClickLisnner != null)
                    onItemClickLisnner.onItemClick(v, text);

                dismiss();
            }
        });
    }


    private void init() {
        initViews();
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ScreenUtil.getScreenWidth(getActivity()) / 5 * 4, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    public static class TextType implements Serializable {
        public String inputText;
        public int index;
        public String title;
        public String leftText;
        public String rghitText;
        public String inputHintText;
        public int inputType;
        public boolean isHindLeftBtn = true;
        public boolean isHindRghitBtn = true;
        public int inputTextLeng;
        public int inputBg;

        public TextType() {
        }

        public TextType(String inputText) {
            this.inputText = inputText;
        }
    }

}
