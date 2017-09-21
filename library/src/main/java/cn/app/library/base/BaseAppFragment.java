package cn.app.library.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.tbruyelle.rxpermissions.RxPermissions;

import cn.app.library.dialog.flycoDialog.dialog.listener.OnBtnClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.MaterialDialog;
import cn.app.library.dialog.styleddialog.StyledDialog;
import cn.app.library.utils.AppUtils;



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
        StyledDialog.buildLoading(msg);
    }

    /**
     * 隐藏加载提示框
     */
    public void hideLoading() {
        StyledDialog.dismissLoading();
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
                },
                btnClickL
        );
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void showTipsDialog() {
        new AlertDialog.Builder(getHoldingActivity())
                .setTitle("消息")
                .setMessage("当前应用无此权限，该功能暂时无法使用。如若需要，请单击确定按钮进行权限授权！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSettings();
                    }
                }).show();

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


}
