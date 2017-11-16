package cn.app.library.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import cn.app.library.rxeasyhttp.http.utils.Utils;
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
 * @description： 基类Fragment
 */
public abstract class BaseFragment extends RxFragment {

    //是否可见
    protected boolean isViable = false;

    // 标志位，标志Fragment已经初始化完成。
    protected boolean isPrepared = false;

    //标记已加载完成，保证懒加载只能加载一次
    protected boolean hasLoaded = false;

    protected View mInflate;
    /**
     * 所属Activity
     */
    private Activity activity;
    protected int currentpage = 1;//当前页码
    protected int page_size = 15;//页面数据量

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate == null)
            mInflate = inflater.inflate(getContainerId(), container, false);
        ButterKnife.bind(this, mInflate);
        initView(mInflate, savedInstanceState);
        return mInflate;
    }

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
                                // 可添加网络连接判断等
                                if (!Utils.isNetworkAvailable(getActivity())) {
                                    //showToast("网络连接断开");
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycle);
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initView();
        initData();
        setListener();
        if (!isPrepared && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isViable = true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    protected abstract int getContainerId();

    protected void initView() {
    }

    protected void setListener() {
    }


    /**
     * 初始化变量
     */
    protected void initVariable() {
    }

    protected void initData() {
    }

    /**
     * 通过ID查找控件
     *
     * @param viewId 控件资源ID
     * @param <VIEW> 泛型参数，查找得到的View
     * @return 找到了返回控件对象，否则返回null
     */
    final public <VIEW extends View> VIEW findView(@IdRes int viewId) {
        return (VIEW) mInflate.findViewById(viewId);
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mInflate == null) {
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
     * 初始化控件
     *
     * @param view
     * @param savedInstanceState
     */
    protected void initView(View view, Bundle savedInstanceState) {

    }

    /**
     * 启动一个Activity，不传值
     *
     * @param className
     */
    public void goToActivity(Class<?> className) {
        goToActivity(className, null);
    }

    /**
     * 启动一个Activity
     *
     * @param className 将要启动的Activity的类名
     * @param options   传到将要启动Activity的Bundle，不传时为null
     */
    public void goToActivity(Class<?> className, Bundle options) {
        Intent intent = new Intent(activity, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
    }

    /**
     * 启动一个有会返回值的Activity
     *
     * @param className   将要启动的Activity的类名
     * @param options     传到将要启动Activity的Bundle，不传时为null
     * @param requestCode 请求码
     */
    public void goToActivityForResult(Class<?> className, Bundle options,
                                      int requestCode) {
        Intent intent = new Intent(activity, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackGroudAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        isPrepared = false;
        hasLoaded = false;
    }

    /**
     * 获取宿主Activity
     *
     * @return
     */
    protected Activity getHoldingActivity() {
        return activity;
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
        ToastCustomUtils.showShort(getContext(), text);
    }

    public void showSnackbarToast(String text, View view) {
        ToastUtil.showSnackbar(text, view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
