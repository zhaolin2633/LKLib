package cn.app.library.adapter.recycleViewCommonAdapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import cn.app.library.adapter.recycleViewCommonAdapter.base.ItemViewDelegate;
import cn.app.library.adapter.recycleViewCommonAdapter.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 * 一种item布局的实现：
 * 这里提供一种item布局，就只需要对MultiItemTypeAdapter<T>进行限定一种layout类型。
 * 并且isForViewType方法返回true，代表着始终返回当前的layout。
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }


            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
