package cn.app.library.base;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.app.library.R;
import cn.app.library.widget.CustomTitlebar;


/**
 * <pre>
 * author : zhaolin
 * time : 2017/08/24
 * desc :有标题列表基类
 * </pre>
 */
public abstract class BaseTitleListActivity extends BaseListActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    protected CustomTitlebar mTitleBar;

    @Override
    protected int getContentView() {
        return R.layout.layout_title_common_refresh_recyclerview;
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar = findView(getTitleBarViewId());
    }

    protected abstract int getTitleBarViewId();

    protected void setTitle(String title, int textColorId) {
        if (mTitleBar != null) {
            mTitleBar.setTilte(title);
            if (textColorId > 0)
                mTitleBar.setTitle_textColor(ContextCompat.getColor(this, textColorId));
        }
    }

    protected void setTitleBarBackcolor(int bgColorId) {
        if (bgColorId > 0) {
            if (mTitleBar != null) {
                mTitleBar.setTitleBarBackground(bgColorId);
            }
        }
    }

    protected void setTitleBarIcon(int leftResId, int rightResId) {
        if (mTitleBar != null) {
            if (leftResId > 0) {
                mTitleBar.setLeftIcon(leftResId);
            }
            mTitleBar.setRightIcon(rightResId);
        }
    }

    protected void setTitleBarRightText(String rightText, int textColorId) {
        if (mTitleBar != null) {
            if (!TextUtils.isEmpty(rightText)) {
                mTitleBar.setTvRight(rightText);
            }
            if (textColorId > 0)
                mTitleBar.setTvRightTextColor(ContextCompat.getColor(this, textColorId));
        }
    }

}
