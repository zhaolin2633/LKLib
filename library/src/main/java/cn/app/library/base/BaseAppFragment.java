package cn.app.library.base;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jaeger.library.StatusBarUtil;

import cn.app.library.dialog.flycoDialog.dialog.listener.OnBtnClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.MaterialDialog;
import cn.app.library.dialog.styleddialog.StyledDialog;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.widget.dialog.DialogMaker;


/**
 * Describe：基于业务需求的基类Fragment
 */

public abstract class BaseAppFragment extends BaseFragment {


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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 跳转至系统设置界面
     */
    private void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
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

    /**
     * 展示对话框
     *
     * @param title     标题
     * @param message   提示信息
     * @param strLeft
     * @param strRight
     * @param btnClickR 确定功能按钮事件监听
     */
    protected void showDialog(final MaterialDialog materialDialog, String title, String message, String strLeft, String strRight, OnBtnClickL onBtnClickL, OnBtnClickL btnClickR) {
        materialDialog
                .title(title)
                .titleTextSize(17)
                .content(message)
                .btnText(strLeft, strRight)
                .show();
        materialDialog.setOnBtnClickL(
                onBtnClickL,
                btnClickR
        );
    }

    /**
     * 展示对话框
     *
     * @param title     标题
     * @param message   提示信息
     * @param btnClickL 确定功能按钮事件监听
     */
    protected void showDialog(final MaterialDialog materialDialog, String title, String message, OnBtnClickL btnClickL) {
        materialDialog
                .title(title)
                .titleTextSize(17)
                .content(message)
                .btnText("取消", "确定")
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
                btnClickL, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        materialDialog.dismiss();
                    }
                }
        );
    }

    /**
     * 展示对话框
     * 单个按钮
     *
     * @param message   提示信息
     * @param strRight
     */
    protected MaterialDialog showDialogSingle(String message, String strRight) {
        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog
                .isTitleShow(false)
                .titleTextSize(17)
                .content(message)
                .btnText(strRight)
                .show();
        materialDialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                materialDialog.dismiss();
            }
        });
        return materialDialog;
    }

    /**
     * 展示对话框
     * 单个按钮
     *
     * @param message   提示信息
     * @param strRight
     */
    protected MaterialDialog showDialogSingle(String title, String message, String strRight) {
        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog
                .title(title)
                .titleTextSize(17)
                .content(message)
                .btnText(strRight)
                .show();
        materialDialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                materialDialog.dismiss();
            }
        });
        return materialDialog;
    }
}
