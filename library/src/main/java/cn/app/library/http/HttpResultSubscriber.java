package cn.app.library.http;

import android.accounts.NetworkErrorException;
import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.app.library.utils.LogUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * BaseObserver
 * Created by jaycee on 2017/6/23.
 */
public abstract class HttpResultSubscriber<T> implements Observer<HttpResult<T>> {

    private static final String TAG = "BaseObserver";
    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 证书出错
     */
    public static final int SSL_ERROR = 1005;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (t.isHttpSuccess()) {
            onSuccess(t.getData());
        } else {
            _onError(t.getMsg(), t.getCode());
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.logError("==error==", e.getMessage() + "===");
        //在这里做全局的错误处理
        if (e instanceof HttpException ||
                e instanceof SocketTimeoutException ||
                e instanceof TimeoutException ||
                e instanceof UnknownHostException) {
            //网络错误
            _onError("服务器连接失败", NETWORK_ERROR);
        } else if (e instanceof ConnectException ||
                e instanceof NetworkErrorException) {
            //网络错误
            _onError("网络连接异常", NETWORK_ERROR);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //数据解析错误
            _onError("数据解析错误", ApiException.PARSE_ERROR);
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            //自定义的ApiException
            _onError(apiException.getErrorMsg(), apiException.getErrCode());
        } else if (e instanceof javax.net.ssl.SSLException) {
            _onError("证书验证失败", SSL_ERROR);
        } else {
            _onError("未知错误", ApiException.UNKNOWN);
        }
        onFinished();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
    }

    public abstract void onSuccess(T t);

    public abstract void _onError(String msg, int code);

    /**
     * 成功或失败到最后都会调用
     */
    public abstract void onFinished();
}
