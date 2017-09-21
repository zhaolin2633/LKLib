package cn.app.library.http.config;

import cn.app.library.http.HttpResult;
import cn.app.library.service.AdEntity;
import io.reactivex.Observable;


/**
 * <pre>
 * author : zhaolin
 * time : 2017/09/18
 * desc :
 * </pre>
 */
public interface  BaseApiService {
    /**
     * 获取启动页广告图片
     *
     * @return
     */
    Observable<HttpResult<AdEntity>> getStartAdImage();
}
