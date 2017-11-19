package cn.app.library.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import cn.app.library.R;
import cn.app.library.base.BaseDialogFragment;
import cn.app.library.utils.ScreenUtil;


/**
 * Created by zhaolin on 2017/5/25.
 * 文字输入弹出框
 */

public class TipsDialogFragment extends BaseDialogFragment {
    TextView ed_input;
    TextView tv_title;
    TextView tv_cancel;
    TextView tv_ok;
    TextType text;

    public static TipsDialogFragment newInstance(TextType text) {
        Bundle args = new Bundle();
        args.putSerializable("data", text);
        TipsDialogFragment fragment = new TipsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
        init();
    }

    public void setOnItemClickLisnner(OnItemClickLisnner onItemClickLisnner) {
        this.onItemClickLisnner = onItemClickLisnner;
    }

    OnItemClickLisnner onItemClickLisnner;

    public interface OnItemClickLisnner {
        public void onLeftClick(View v, TextType bean);

        public void onRightClick(View v, TextType bean);
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
        if (text != null && text.leftTextColor > 0) {
            tv_cancel.setTextColor(ContextCompat.getColor(getActivity(), text.leftTextColor));
        }
        if (text != null && text.rghitTextColor > 0) {
            tv_ok.setTextColor(ContextCompat.getColor(getActivity(), text.rghitTextColor));
        }
        tv_ok.setText(text != null && !TextUtils.isEmpty(text.rghitText) ? text.rghitText : "完成");
        ed_input.setText(text.inputText);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLisnner != null)
                    onItemClickLisnner.onLeftClick(v, text);
                dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLisnner != null)
                    onItemClickLisnner.onRightClick(v, text);
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
        public int rghitTextColor;
        public int leftTextColor;
        public boolean isHindLeftBtn = true;
        public boolean isHindRghitBtn = true;

        public TextType() {
        }

        public TextType(String inputText) {
            this.inputText = inputText;
        }
    }

}
