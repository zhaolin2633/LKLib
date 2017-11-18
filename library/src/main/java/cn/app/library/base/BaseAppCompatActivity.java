package cn.app.library.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import cn.app.library.R;
import cn.app.library.dialog.flycoDialog.dialog.listener.OnBtnClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.MaterialDialog;
import cn.app.library.dialog.styleddialog.StyledDialog;
import cn.app.library.picture.lib.permissions.Permission;
import cn.app.library.picture.lib.permissions.RxPermissions;
import cn.app.library.rxeasyhttp.http.utils.Utils;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.widget.toast.ToastCustomUtils;
import cn.app.library.widget.toast.ToastTextUtil;
import cn.app.library.widget.toast.ToastUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @description： 基类Activity
 */
public abstract class BaseAppCompatActivity extends RxAppCompatActivity {

    private static final String TAG = "BaseFragmentActivity";
    private BroadcastReceiver broadcastReceiver;
    public static int REQUEST_CODE = 0;
    private boolean destroyed = false;

    /**
     * 线程调度
     */
    protected <T> ObservableTransformer<T, T> transformer(final LifecycleTransformer<T> lifecycle) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {

                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycle);
            }
        };
    }

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
        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
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
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
                return connectivityManager.getActiveNetworkInfo().isAvailable();
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    private void initReceiver() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent!=null&&intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
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
     * @param message  提示信息
     * @param strRight
     */
    protected MaterialDialog showDialogSingle(String message, String strRight) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
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
     * @param message  提示信息
     * @param strRight
     */
    protected MaterialDialog showDialogSingle(String title, String message, String strRight) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
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

    /**
     * 跳转至系统设置界面
     */
    private void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    protected View statusBarView;

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        if (getStatusBarId() > 0) {
            statusBarView = findView(getStatusBarId());
            statusBarView.getLayoutParams().height = ScreenUtil.getStatusHeight(this);
            statusBarView.getLayoutParams().width = ScreenUtil.getScreenWidth(this);
            StatusBarUtil.setTransparent(this);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, getStatusBarBgId() > 0 ? getStatusBarBgId() : R.color.black), 0);
        }
    }

    protected abstract int getStatusBarId();

    protected abstract int getStatusBarBgId();

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

    public void showLoding(String msg) {
        StyledDialog.buildLoading(msg).show();
    }

    public void dismissLoading() {
        StyledDialog.dismissLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
