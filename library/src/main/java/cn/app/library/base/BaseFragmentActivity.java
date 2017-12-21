package cn.app.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;


import cn.app.library.dialog.flycoDialog.dialog.listener.OnBtnClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.MaterialDialog;
import cn.app.library.widget.dialog.DialogMaker;
import cn.app.library.widget.toast.ToastCustomUtils;
import cn.app.library.widget.toast.ToastTextUtil;
import cn.app.library.widget.toast.ToastUtil;

/**
 * @description： 基类FragmentActivity
 */
public class BaseFragmentActivity extends FragmentActivity {


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
        if (outState != null) {
            String FRAGMENTS_TAG = "Android:support:fragments";
            outState.remove(FRAGMENTS_TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void findViews() {

    }

    protected void initViews() {

    }

    protected void registerViews() {

    }

    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    /**
     * 展示对话框
     *
     * @param message   提示信息
     * @param strLeft
     * @param strRight
     * @param btnClickL 确定功能按钮事件监听
     */
    protected void showDialog(final MaterialDialog materialDialog, String message, String strLeft, String strRight, OnBtnClickL btnClickL) {
        materialDialog
                .isTitleShow(false)
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

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void goToActivity(Class<?> clz) {
        goToActivity(clz, null, false);
    }

    public void goToActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        goToActivity(clz, null, isCloseCurrentActivity);
    }

    public void goToActivity(Class<?> clz, Bundle options) {
        goToActivity(clz, options, false);
    }

    /**
     * 启动一个Activity
     *
     * @param className 将要启动的Activity的类名
     * @param options   传到将要启动Activity的Bundle，不传时为null
     */
    protected void goToActivity(Class<?> className, Bundle options, boolean isCloseCurrentActivity) {
        Intent intent = new Intent(this, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
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
        ToastTextUtil.getInstance(this).show(text);
    }

    public void showCustomToast(String text) {
        ToastCustomUtils.showShort(this, text);
    }

    public void showSnackbarToast(String text, View view) {
        ToastUtil.showSnackbar(text, view);
    }

    /**
     * 显示加载提示框
     */
    public void showLoding(boolean cancle) {
        showLoding("加载中...", cancle);
    }


    public void showLoding(String msg) {
        if (!DialogMaker.isShowing()) {
            DialogMaker.showProgressDialog(this, msg);
        }
    }

    public void showLoding(String msg, boolean cancle) {
        if (!DialogMaker.isShowing()) {
            DialogMaker.showProgressDialog(this, msg, cancle);
        }
    }

    /**
     * 隐藏加载提示框
     */
    public void dismissLoading() {
        if (DialogMaker.isShowing()) {
            DialogMaker.dismissProgressDialog();
        }
    }
}
