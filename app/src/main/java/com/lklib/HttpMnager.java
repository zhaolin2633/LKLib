package com.lklib;

import android.content.Context;

import com.lklib.http.Api;

import cn.app.library.http.BaseHttpManager;
import cn.app.library.http.config.Constants;
import cn.app.library.utils.AppLibInitTools;
import cn.app.library.utils.AppUtils;
import cn.app.library.utils.DeviceUuidFactory;
import cn.app.library.utils.LogUtil;
import cn.app.library.utils.SignatureUtil;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/14
 * desc :
 * </pre>
 */
public class HttpMnager extends BaseHttpManager{

    private volatile static HttpMnager instance;

    public static HttpMnager getInstance() {
        if (instance == null) {
            synchronized (HttpMnager.class) {
                if (instance == null) {
                    instance = new HttpMnager();
                }
            }
        }
        return instance;
    }

    @Override
    public String api() {
        return Api.IP+AppUtils.getAppVersionName(AppLibInitTools.appContext) + "/android/";
    }

    @Override
    public Request.Builder getRequestBuilder(Request.Builder requestBuilder) {
        final long currentTimeMillis = System.currentTimeMillis();
        requestBuilder.addHeader("key-random", Constants.APP_RANDOM)
                .addHeader("key-time", String.valueOf(currentTimeMillis))
                .addHeader("key-id", Constants.KEY_ID)
                .addHeader("app-id", new DeviceUuidFactory(AppLibInitTools.appContext).getDeviceUuid().toString());
        LogUtil.logInfo("Header=====>access-token", "");
        return requestBuilder;
    }

    @Override
    public Request.Builder getAppInitRequestBuilder(Request.Builder requestBuilder) {
        final long currentTimeMillis = System.currentTimeMillis();
        requestBuilder.addHeader("key-random", Constants.APP_RANDOM)
                .addHeader("key-time", String.valueOf(currentTimeMillis))
                .addHeader("key-id", Constants.KEY_ID)
                .addHeader("app-id", new DeviceUuidFactory(AppLibInitTools.appContext).getDeviceUuid().toString())
                .addHeader("key-signature", SignatureUtil.NoSignUtil(Constants.APP_RANDOM, String.valueOf(currentTimeMillis)));
        LogUtil.logInfo("Header=====>access-token", "");
        return requestBuilder;
    }

    @Override
    public Request.Builder getWithCacheRequestBuilder(Request.Builder requestBuilder) {
        final long currentTimeMillis = System.currentTimeMillis();
        requestBuilder.addHeader("key-random", Constants.APP_RANDOM)
                .addHeader("key-time", String.valueOf(currentTimeMillis))
                .addHeader("key-id", Constants.KEY_ID)
                .addHeader("app-id", new DeviceUuidFactory(AppLibInitTools.appContext).getDeviceUuid().toString())
                .addHeader("key-signature", SignatureUtil.NoSignUtil(Constants.APP_RANDOM, String.valueOf(currentTimeMillis)));
        LogUtil.logInfo("Header=====>access-token", "");
        return requestBuilder;
    }

    @Override
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return null;
    }

}
