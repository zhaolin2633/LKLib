package cn.app.library.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.app.library.R;
import cn.app.library.dialog.flycoDialog.dialog.listener.OnBtnClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.MaterialDialog;
import cn.app.library.ui.zixing.helper.CaptureHelper;
import cn.app.library.utils.AppLibInitTools;
import cn.app.library.widget.toast.SnackbarUtil;
import cn.app.library.widget.toast.ToastCustomUtils;
import cn.app.library.widget.toast.ToastTextUtil;
import cn.app.library.widget.toast.ToastUtil;
import cn.app.library.widget.toast.Toasty;



/**
 * @description： 基类Activity
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private static final String TAG = "BaseFragmentActivity";
    private BroadcastReceiver broadcastReceiver;
    public static int REQUEST_CODE = 0;
    private boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除系统标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Logger.d("name (%s.java:0)", getClass().getSimpleName());
        setContentView(getContentView());
        ButterKnife.bind(this);
        setStatusBar();
        initVariable();
        initView();
        initData();
        initReceiver();
        setListener();
    }

    protected abstract int getContentView();

    protected void initView() {
    }

    protected void setListener() {
    }

    /**
     * 初始化变量
     */
    protected void initVariable() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 获取当前view的LayoutInflater实例
     *
     * @return
     */
    protected LayoutInflater getLayouInflater() {
        LayoutInflater _LayoutInflater = LayoutInflater.from(this);
        return _LayoutInflater;
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

    /**
     * 启动一个有会返回值的Activity
     *
     * @param className   将要启动的Activity的类名
     * @param options     传到将要启动Activity的Bundle，不传时为null
     * @param requestCode 请求码
     */
    protected void goToActivityForResult(Class<?> className, Bundle options,
                                         int requestCode) {
        Intent intent = new Intent(this, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 判断是否连接网络
     *
     * @return
     */
    protected boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null&&connectivityManager.getActiveNetworkInfo() != null) {
            return connectivityManager.getActiveNetworkInfo().isAvailable();
        } else {
            return false;
        }
    }

    private void initReceiver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (!isConnected()) {
                        ToastCustomUtils.showShort(BaseAppCompatActivity.this, "网络连接断开，请检查网络设置");
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    /**
     * 控件绑定
     *
     * @param resId
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }


    public void requestPermission(String[] permissions, int requestCode) {
        REQUEST_CODE = requestCode;

        //检查权限是否授权
        if (checkPermissions(permissions)) {
            permissinSucceed(REQUEST_CODE);
        } else {
            List<String> needPermissions = getPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE);
        }
    }

    private List<String> getPermissions(String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (verificationPermissions(grantResults)) {
                permissinSucceed(REQUEST_CODE);
            } else {
                permissionFailing(REQUEST_CODE);
                showTipsDialog();
            }
        }
    }

    private boolean verificationPermissions(int[] results) {

        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(BaseAppCompatActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public void showTipsDialog() {
        new AlertDialog.Builder(this)
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
     * 跳转至系统设置界面
     */
    private void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    public void permissionFailing(int requestCode) {
        Log.d(TAG, "获取权限失败=" + requestCode);
    }


    /**
     * 获取权限失败
     *
     * @param requestCode
     */
    public void permissinSucceed(int requestCode) {
        Log.d(TAG, "获取权限成功=" + requestCode);
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.common_main), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        ButterKnife.unbind(this);
        destroyed = true;
    }
    private static Handler handler;

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }

    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 获取虚拟导航栏的高度
     *
     * @param ctx
     * @return
     */
    protected static float getNormalNavigationBarHeight(final Context ctx) {
        try {
            final Resources res = ctx.getResources();
            int rid = res.getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid > 0) {
                boolean flag = res.getBoolean(rid);
                if (flag) {
                    int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        return res.getDimensionPixelSize(resourceId);
                    }
                }
            }
        } catch (Throwable e) {
            return 0;
        }
        return 0;
    }

    /**
     * 检测是否存在NavigationBar
     */
    public boolean checkDeviceHasNavigationBar() {
        if (getWindowManager().getDefaultDisplay().getHeight() < (getWindowManager().getDefaultDisplay().getWidth() * 16 / 9) - 20) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            CaptureHelper.onActivityResult(this, requestCode, intent);
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Handle cancel
        }
    }
}
