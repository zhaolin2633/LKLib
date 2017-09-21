package com.lklib.http;

import cn.app.library.http.HttpResult;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/21
 * desc :
 * </pre>
 */
public interface ApiService  {
    @GET(Api.TRADE_SERACH_HOT)
    Observable<HttpResult<String>> getString();
}
