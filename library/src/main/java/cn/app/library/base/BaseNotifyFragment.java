package cn.app.library.base;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;


import com.tbruyelle.rxpermissions.RxPermissions;

import cn.app.library.R;
import cn.app.library.dialog.flycoDialog.dialog.listener.OnBtnClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.MaterialDialog;
import cn.app.library.utils.AppUtils;
import cn.app.library.widget.EmptyView;


/**
 * Author:xiaohaibin
 * <p>
 * CrateTime:2016-12-06 14:48
 * <p>
 * Description:带loading提示及各种状态视图的Fragment
 */
public class BaseNotifyFragment extends BaseNormalFragment implements EmptyView.OnRefreshClickListener {

    private LinearLayout loadingLayout;
    private ViewStub emptyViewStub;
    private View emptyView;
    private View contentView;
    protected int currentpage = 1;//当前页码
    protected int page_size = 15;//页面数据量

    @Override
    protected void setContentView(@LayoutRes int layoutId) {
        super.setContentView(R.layout.fragment_base);
        emptyViewStub = (ViewStub) getBaseView().findViewById(R.id.base_fragment_empty);

        loadingLayout = (LinearLayout) getBaseView().findViewById(R.id.loading);

        ViewStub contentViewStub = (ViewStub) getBaseView().findViewById(R.id.base_fragment_content);
        contentViewStub.setLayoutResource(layoutId);
        contentView = contentViewStub.inflate();
    }

    private View findViewById(@IdRes int id) {
        return contentView.findViewById(id);
    }

    @Override
    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    public View getContentView() {
        return contentView;
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

    protected void loadingEmpty() {
        loadingEmpty("暂无数据");
    }

    protected void loadingError() {
        loadingEmpty("加载失败，点击重试！");
    }

    protected void loadingEmpty(String label) {
        loadingEmpty(label, 0);
    }

    protected void loadingEmpty(String label, @DrawableRes int image) {
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

    protected void addTextButton(String text, View.OnClickListener onClickListener) {
        addTextButton(text, getResources().getColor(R.color.black_90), onClickListener);
    }

    protected void addTextButton(String text, int textColor, View.OnClickListener onClickListener) {
        getContainerActivity().addTextButton(text, textColor, onClickListener);
    }


    /**
     * 展示对话框
     *
     * @param title     标题
     * @param message   提示信息
     * @param strLeft
     * @param strRight
     * @param btnClickL 确定功能按钮事件监听
     */
    protected void showDialog(final MaterialDialog materialDialog, String title, String message, String strLeft, String strRight, OnBtnClickL btnClickL) {
        materialDialog
                .title(title)
                .titleTextSize(17)
                .content(message)
                .btnText(strLeft, strRight)
                .show();
        materialDialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        materialDialog.dismiss();
                    }
                }, btnClickL
        );
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
