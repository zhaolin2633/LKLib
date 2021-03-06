package cn.app.library.base;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.List;

import cn.app.library.R;
import cn.app.library.utils.ScreenUtil;


/**
 * <pre>
 * author : zhaolin
 * time : 2017/08/24
 * desc :列表基类
 * </pre>
 */
public abstract class BaseListActivity extends BaseAppCompatActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    protected EasyRecyclerView mRecyclerView;
    protected int currentpage = 1;//当前页码
    protected int page_size = 15;//页面数据量

    @Override
    protected int getContentView() {
        return R.layout.layout_common_refresh_recyclerview;
    }

    protected void initRecyclerView() {
        mRecyclerView = findView(getRecyclerViewId());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setRefreshingColor(Color.rgb(0, 140, 240), Color.rgb(0, 140, 240), Color.rgb(0, 140, 240));
    }

    @Override
    protected void initView() {
        super.initView();
        initRecyclerView();
    }

    protected abstract int getRecyclerViewId();

    protected void setEmptyView(String content) {
        if (mRecyclerView != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
            TextView tv_emptyText = (TextView) view.findViewById(R.id.empty_view_tv);
            if (!TextUtils.isEmpty(content))
                tv_emptyText.setText(content);
            mRecyclerView.setEmptyView(view);
        }
    }

    protected void setEmptyView(String content, int resId) {
        if (mRecyclerView != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
            ImageView iv_nodata = (ImageView) view.findViewById(R.id.iv_nodata);
            TextView tv_emptyText = (TextView) view.findViewById(R.id.empty_view_tv);
            if (!TextUtils.isEmpty(content))
                tv_emptyText.setText(content);
            if (resId > 0)
                iv_nodata.setImageResource(resId);
            mRecyclerView.setEmptyView(view);
        }
    }

    protected void addRecyclerViewDecoration() {
        DividerDecoration dividerDecoration = new DividerDecoration(getResources().getColor(R.color.color_eeeeee),
                ScreenUtil.dip2px(this, 1));
        dividerDecoration.setDrawLastItem(false);
        mRecyclerView.addItemDecoration(dividerDecoration);
    }

    protected abstract void onRefreshLoadData(int currentpage);

    protected void setRecyclerViewAdapterAttribute(final RecyclerArrayAdapter adapter, boolean hasRefresh, boolean hasLoadMore) {
        if (mRecyclerView != null && adapter != null) {
            if (hasRefresh) {
                mRecyclerView.setRefreshListener(this);
            }
            if (hasLoadMore) {
                adapter.setMore(R.layout.view_more, this);
                adapter.setNoMore(R.layout.view_nomore);
                adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
                    @Override
                    public void onErrorShow() {
                        adapter.resumeMore();
                    }

                    @Override
                    public void onErrorClick() {
                        adapter.resumeMore();
                    }
                });
            }
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    itemClick(adapter.getItem(position), position);
                }
            });
        }
    }

    protected void showRecyclerViewRefreshing() {
        if (mRecyclerView != null) {
            mRecyclerView.setRefreshing(true);
        }
    }

    protected void hideRecyclerViewRefreshing() {
        if (mRecyclerView != null) {
            mRecyclerView.setRefreshing(false);
        }
    }

    protected void itemClick(Object object, int position) {

    }

    protected void setAdapter(RecyclerArrayAdapter adapter, int page, List list) {
        if (page == 1) {
            adapter.clear();
        }
        adapter.addAll(list);
        if (adapter.getCount() < page_size) {
            adapter.stopMore();
        }
    }

    @Override
    public void onRefresh() {
        currentpage = 1;
        onRefreshLoadData(currentpage);
    }

    @Override
    public void onLoadMore() {
        currentpage++;
        onRefreshLoadData(currentpage);
    }
}
