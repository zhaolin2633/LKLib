package cn.app.library.http;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.app.library.http.https.HttpsUtils;
import cn.app.library.utils.AppLibInitTools;
import cn.app.library.utils.AppUtils;
import cn.app.library.utils.NetworkUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @describe: HTTP请求工厂类
 */
public abstract class BaseHttpManager {

    public static boolean isDebug = false;


    public void DeBug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public abstract String api();

    public abstract Request.Builder getRequestBuilder(Request.Builder requestBuilder);

    public abstract Request.Builder getAppInitRequestBuilder(Request.Builder requestBuilder);

    public abstract Request.Builder getWithCacheRequestBuilder(Request.Builder requestBuilder);

    /**
     * 无缓存模式
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api())
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);

    }

    /**
     * 有缓存模式
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public <S> S createServiceWithCache(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api())
                .client(getOkHttpClientWithCache())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);

    }


    /**
     * App初始化获取、刷新Key
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public <S> S createAppInitService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api())
                .client(getAppInitOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }


    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            if (isDebug)
                Logger.i(message);
        }
    });

    private static final long DEFAULT_TIMEOUT = 5;

    private OkHttpClient getOkHttpClient() {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //支持https
        builder.sslSocketFactory(sslParams.sSLSocketFactory);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                getRequestBuilder(requestBuilder)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return builder.build();
    }

    /**
     * 获取App秘钥
     *
     * @return
     */
    private OkHttpClient getAppInitOkHttpClient() {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //支持https
        builder.sslSocketFactory(sslParams.sSLSocketFactory);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                getAppInitRequestBuilder(requestBuilder)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        });

        return builder.build();
    }


    /**
     * 带缓存模式
     *
     * @return
     */
    private OkHttpClient getOkHttpClientWithCache() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        //定制OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //支持https
        builder.sslSocketFactory(sslParams.sSLSocketFactory);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        //debug模式开启日志输出
        loggingInterceptor.setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(loggingInterceptor);

        //设置缓存
        File httpCacheDirectory = new File(AppLibInitTools.appContext.getCacheDir(), AppUtils.getAppVersionName(AppLibInitTools.appContext) + "Appcache");
        builder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                getWithCacheRequestBuilder(requestBuilder)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                if (NetworkUtil.isNetAvailable(AppLibInitTools.appContext)) {
                    int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
                    // 如果单个请求不同请在请求中写上Cache-control头则按照对应的配置进行本地缓存时间配置
                    String cacheControl = request.cacheControl().toString();
                    if (TextUtils.isEmpty(cacheControl)) {
                        return response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        return response.newBuilder()
                                .header("Cache-Control", cacheControl)
                                .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    }
                } else {
                    // 无网络时，设置超时为1周
                    int maxStale = 60 * 60 * 24 * 7;
                    return response.newBuilder()
                            .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        });
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtil.isNetworkConnected(AppLibInitTools.appContext)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);
            }
        });
        return builder.build();
    }


}
