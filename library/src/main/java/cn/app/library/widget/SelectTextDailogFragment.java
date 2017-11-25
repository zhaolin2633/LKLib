package cn.app.library.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

import cn.app.library.R;
import cn.app.library.adapter.recycleViewCommonAdapter.CommonAdapter;
import cn.app.library.adapter.recycleViewCommonAdapter.base.ViewHolder;
import cn.app.library.base.BaseDialogFragment;
import cn.app.library.utils.GsonUtil;
import cn.app.library.utils.ScreenUtil;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/11/09
 * desc :条件选择对话框
 * </pre>
 */
public class SelectTextDailogFragment extends BaseDialogFragment {

    TextView tv_title;
    RecyclerView recyclerView;
    List<TextType> mList;
    TextType selectType;
    String title;
    boolean isShowTitle = true;

    public static SelectTextDailogFragment newInstance(String title, List<TextType> list, TextType selectType) {
        Bundle args = new Bundle();
        SelectTextDailogFragment fragment = new SelectTextDailogFragment();
        args.putString("list", GsonUtil.newGson().toJson(list));
        args.putString("title", title);
        args.putSerializable("selectType", selectType);
        fragment.setArguments(args);
        return fragment;
    }

    public static SelectTextDailogFragment newInstance(String title, boolean isShowTitle, List<TextType> list, TextType selectType) {
        Bundle args = new Bundle();
        SelectTextDailogFragment fragment = new SelectTextDailogFragment();
        args.putString("list", GsonUtil.newGson().toJson(list));
        args.putString("title", title);
        args.putBoolean("isShowTitle", isShowTitle);
        args.putSerializable("selectType", selectType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_text);
        findViews();
        initViews();
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ScreenUtil.getScreenWidth(getActivity()), ScreenUtil.getScreenWidth(getActivity()) / 3 * 2);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void findViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }

    @Override
    protected void initViews() {
        title = getArguments().getString("title");
        isShowTitle = getArguments().getBoolean("isShowTitle", true);
        String json = getArguments().getString("list");
        if (!TextUtils.isEmpty(json)) {
            mList = GsonUtil.newGson().fromJson(json, new TypeToken<List<TextType>>() {
            }.getType());
        }
        selectType = (TextType) getArguments().getSerializable("selectType");
        tv_title.setText(!TextUtils.isEmpty(title) ? title : "选择");
        tv_title.setVisibility(isShowTitle ? View.VISIBLE : View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CommonAdapter<TextType>(getActivity(), R.layout.dialog_select_text_item, mList) {

            @Override
            protected void convert(ViewHolder viewHolder, final TextType textType, final int i) {
                TextView tv_name = viewHolder.getView(R.id.tv_name);
                ImageView iv_select = viewHolder.getView(R.id.iv_select);
                tv_name.setText(textType.name);
                if (textType.indexTextColor > 0 && textType.index == i) {
                    tv_name.setTextColor(ContextCompat.getColor(getActivity(), textType.indexTextColor));
                } else {
                    tv_name.setTextColor(ContextCompat.getColor(getActivity(), textType.defaultTextColor > 0 ? textType.defaultTextColor : R.color.color_888888));
                }
                if (selectType == null) {
                    if (i == 0) {
                        iv_select.setVisibility(View.VISIBLE);
                    } else {
                        iv_select.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (textType.name.equals(selectType.name)) {
                        iv_select.setVisibility(View.VISIBLE);
                    } else {
                        iv_select.setVisibility(View.INVISIBLE);
                    }
                }
                if(textType.selectResId>0){
                    iv_select.setImageResource(textType.selectResId);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (linsener != null)
                            linsener.onItemClick(textType, i);
                    }
                });
            }
        });
    }

    public static class TextType implements Serializable {
        public String name;
        public String content;
        public int id;
        public int indexTextColor;
        public int index = -1;
        public int defaultTextColor;
        public int selectResId;

        public TextType(String name, String content, int id) {
            this.name = name;
            this.content = content;
            this.id = id;
        }

        public TextType setDefaultTextColor(int defaultTextColor) {
            this.defaultTextColor = defaultTextColor;
            return this;
        }

        public TextType setIndex(int index) {
            this.index = index;
            return this;
        }

        public TextType setIndexTextColor(int indexTextColor) {
            this.indexTextColor = indexTextColor;
            return this;
        }
        public TextType setSelectResId(int selectResId) {
            this.selectResId = selectResId;
            return this;
        }
    }

    public OnItemClickLinsener linsener;

    public void setOnItemClickLinsener(OnItemClickLinsener linsener) {
        this.linsener = linsener;
    }

    public interface OnItemClickLinsener {
        public void onItemClick(TextType textType, int pos);
    }
}
