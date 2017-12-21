package cn.app.library.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.app.library.R;
import cn.app.library.widget.dialog.DialogMaker;
import cn.app.library.widget.toast.ToastCustomUtils;
import cn.app.library.widget.toast.ToastTextUtil;
import cn.app.library.widget.toast.ToastUtil;

/**
 * Author:xiaohaibin
 * <p>
 * CrateTime:2016-12-6 10:10:18
 * <p>
 * Description:Fragment基类
 */
public class BaseNormalFragment extends Fragment {

    private View baseView;

    //是否可见
    protected boolean isViable = false;

    // 标志位，标志Fragment已经初始化完成。
    protected boolean isPrepared = false;

    //标记已加载完成，保证懒加载只能加载一次
    protected boolean hasLoaded = false;


    protected BaseFragmentActivity getBaseActivity() {
        return (BaseFragmentActivity) getActivity();
    }

    protected ContainerFragmentActivity getContainerActivity() {
        return (ContainerFragmentActivity) getActivity();
    }

    protected void setContentView(@LayoutRes int layoutId) {
        baseView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return baseView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isPrepared && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isViable = true;
        }
    }

    public View getBaseView() {
        return baseView;
    }

    private View findViewById(@IdRes int id) {
        return baseView.findViewById(id);
    }

    /**
     * 省略findViewById的强制类型转换
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected void findViews() {
    }

    protected void initViews() {
    }

    protected void registerViews() {
    }

    protected void clearTitleButton() {
        getContainerActivity().clearActionButton();
    }


    protected void addImageButton(int imageResId, String tag, View.OnClickListener onClickListener) {
        getContainerActivity().addImageButton(imageResId, tag, onClickListener);
    }

    protected void addImageButton(int imageResId, String tag, View.OnClickListener onClickListener, int padding) {
        getContainerActivity().addImageButton(imageResId, tag, onClickListener, padding);
    }

    protected void addTextButton(String text, View.OnClickListener onClickListener) {
        addTextButton(text, getResources().getColor(R.color.black_90), onClickListener);
    }

    protected void addTextButton(String text, int textColor, View.OnClickListener onClickListener) {
        getContainerActivity().addTextButton(text, textColor, onClickListener);
    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     *
     * @param isVisible
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            onVisible();
        } else {
            onInVisible();
        }

    }

    /**
     * 当界面可见时的操作
     */
    protected void onVisible() {
        if (hasLoaded) {
            return;
        }
        lazyLoad();
        hasLoaded = true;
    }


    /**
     * 数据懒加载
     */
    protected void lazyLoad() {

    }

    /**
     * 当界面不可见时的操作
     */
    protected void onInVisible() {

    }


    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (baseView == null) {
            return;
        }
        isPrepared = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isViable = true;
            return;
        }

        if (isViable) {
            onFragmentVisibleChange(false);
            isViable = false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void showToast(ToastUtil.ToastType type, CharSequence text) {
        ToastUtil.show(type, text);
    }

    public void showToast(CharSequence text) {
        ToastUtil.show(text);
    }

    public void showToast(CharSequence text, int resId) {
        ToastUtil.show(text, resId);
    }

    public void showTextToast(CharSequence text) {
        ToastTextUtil.getInstance(getActivity()).show(text);
    }

    public void showCustomToast(String text) {
        ToastCustomUtils.showShort(getActivity(), text);
    }

    public void showSnackbarToast(String text, View view) {
        ToastUtil.showSnackbar(text, view);
    }
    /**
     * 显示加载提示框
     */
    public void showLoading() {
        showLoading("加载中...");
    }

    public void showLoading(String msg) {
        if (!DialogMaker.isShowing()) {
            DialogMaker.showProgressDialog(getContext(), msg);
        }
    }

    /**
     * 隐藏加载提示框
     */
    public void hideLoading() {
        if (DialogMaker.isShowing()) {
            DialogMaker.dismissProgressDialog();
        }
    }

}
