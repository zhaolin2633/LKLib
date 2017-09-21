package cn.app.library.http;

import com.trello.rxlifecycle2.LifecycleTransformer;

import cn.app.library.rxeasyhttp.http.utils.Utils;
import cn.app.library.utils.AppLibInitTools;
import cn.app.library.widget.toast.ToastUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/20
 * desc :
 * </pre>
 */
public class ObservableTransformer {
    /**
     * 线程调度
     */
    protected <T> io.reactivex.ObservableTransformer<T, T> transformer(final LifecycleTransformer<T> lifecycle) {
        return new io.reactivex.ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                // 可添加网络连接判断等
                                if (!Utils.isNetworkAvailable(AppLibInitTools.appContext)) {
                                    ToastUtil.show(ToastUtil.ToastType.ERROR, "网络连接断开");
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycle);
            }
        };
    }
}
