package cn.app.library.base;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;

import cn.app.library.R;
import cn.app.library.widget.CustomTitlebar;
import cn.app.library.widget.EmptyView;

/**
 * Author：xiaohaibin
 * Time：2017/6/23
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：带loading提示及各种状态视图的BaseActivity
 */
public abstract class BaseNotifyActivity extends BaseAppCompatActivity implements EmptyView.OnRefreshClickListener {

    private LinearLayout loadingLayout;
    private ViewStub emptyViewStub;
    private View emptyView;
    private View contentView;
    private CustomTitlebar mCustomTitlebar;

    @Override
    public void setContentView(@LayoutRes int layoutId) {
        super.setContentView(R.layout.activity_base);
        emptyViewStub = (ViewStub) findViewById(R.id.base_activity_empty);
        loadingLayout = (LinearLayout) findViewById(R.id.loading);

        ViewStub contentViewStub = (ViewStub) findViewById(R.id.base_activity_content);
        contentViewStub.setLayoutResource(layoutId);
        contentView = contentViewStub.inflate();

        mCustomTitlebar = (CustomTitlebar) findViewById(R.id.title_bar);
        mCustomTitlebar.setTitleBarOnClickListener(new CustomTitlebar.SimpleOnClickListener() {
            @Override
            public void onClickTitleLeft() {
                finish();
            }
        });
    }

    public void setTitle(String title) {
        if (mCustomTitlebar != null) {
            mCustomTitlebar.setTilte(title);
        }
    }

    public CustomTitlebar getCustomTitlebar() {
        return mCustomTitlebar;
    }

    private View findViewWithId(@IdRes int id) {
        return contentView.findViewById(id);
    }

    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewWithId(id);
    }

    public void setEmptyLayoutId(@LayoutRes int emptyLayoutId) {
        emptyViewStub.setLayoutResource(emptyLayoutId);
        emptyView = emptyViewStub.inflate();
        emptyView.setVisibility(View.GONE);
    }

    /**
     * 检查空数据提示是否初始化
     * <p>
     * 若为初始化，则使用默认空提示布局进行 初始化
     */
    private void checkEmptyView() {
        if (emptyView == null) {
            setEmptyLayoutId(R.layout.fragment_base_empty);
        }
    }

    protected void showLoading() {
        checkEmptyView();
        loadingLayout.setBackgroundResource(android.R.color.transparent);
        emptyView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    protected void showLoading(String text) {
        checkEmptyView();
        loadingLayout.setBackgroundResource(android.R.color.transparent);
        emptyView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        checkEmptyView();
        loadingLayout.setVisibility(View.GONE);
    }

    protected void showLoadingWithBackground() {
        checkEmptyView();
        loadingLayout.setBackgroundResource(R.color.common_background);
        emptyView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    protected void showEmpty() {
        showEmpty("暂无数据");
    }

    protected void showError() {
        showEmpty("加载失败，点击重试！");
    }

    protected void showEmpty(String label) {
        showEmpty(label, 0);
    }

    protected void showEmpty(String label, @DrawableRes int image) {
        checkEmptyView();
        emptyView.setVisibility(View.VISIBLE);
        //设置默认空数据布局时，强制类型转换会抛出异常
        try {
            ((EmptyView) emptyView).setLabel(label);
            ((EmptyView) emptyView).setImage(image);
            ((EmptyView) emptyView).setRefreshClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void resetLoading() {
        checkEmptyView();
        emptyView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.common_main), 0);
    }

    @Override
    public void OnRefresh() {
        onNetworkViewRefresh();
    }

    /**
     * 重新请求网络
     */
    public void onNetworkViewRefresh() {
    }

}