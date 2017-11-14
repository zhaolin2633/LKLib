package cn.app.library.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

public class TextInputDialogFragment extends BaseDialogFragment {
    ClearEditText ed_input;
    TextView tv_title;
    TextView tv_cancel;
    TextView tv_ok;
    TextType text;

    public static TextInputDialogFragment newInstance(TextType text) {
        Bundle args = new Bundle();
        args.putSerializable("data", text);
        TextInputDialogFragment fragment = new TextInputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_text);
        init();
    }

    public void setOnItemClickLisnner(OnItemClickLisnner onItemClickLisnner) {
        this.onItemClickLisnner = onItemClickLisnner;
    }

    OnItemClickLisnner onItemClickLisnner;

    public interface OnItemClickLisnner {
        public void onItemClick(View v, TextType bean, int pos);
    }

    @Override
    protected void initViews() {
        super.initViews();
        text = (TextType) getArguments().getSerializable("data");
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
            ed_input.setText(text.inputText);
        }
        ed_input.setInputType(text != null && text.inputType > 0 ? text.inputType : InputType.TYPE_CLASS_TEXT);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    onItemClickLisnner.onItemClick(v, text, 0);
                dismiss();
            }
        });
    }


    private void init() {
        initViews();
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ScreenUtil.getScreenWidth(getActivity()) / 5 * 4, ScreenUtil.getScreenWidth(getActivity()) / 2);
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
        public boolean isHindLeftBtn=true;
        public boolean isHindRghitBtn=true;

        public TextType() {
        }

        public TextType(String inputText) {
            this.inputText = inputText;
        }
    }

}
