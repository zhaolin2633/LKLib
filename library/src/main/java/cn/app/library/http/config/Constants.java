package cn.app.library.http.config;


import cn.app.library.base.BaseApplication;

/**
 * Describe：常量相关工具类
 */

public class Constants {

    /**
     * App请求头随机数0-10
     */
    public static final String APP_RANDOM = "445";



    public static final String KEY_ID = "7774e20c20232caa95ce0e0e5d8811b4";

    public static final String INTENT_KEY_DATA = "data";
    public static final String INTENT_KEY_FLAG = "flag";
    public static final String INTENT_KEY_ID = "id";
    public static final String INTENT_KEY_POS = "pos";
    public static final String INTENT_KEY_DATA_LIST = "data_list";

    //万象优图空间名
    public static final String BUCKET_PASSPORT = "passport";



    public static final String EXTRA_KEY_EXIT = "extra_key_exit";
    public static final String DOWNLOAD_SPLASH = "download_splash";
    public static final String EXTRA_DOWNLOAD = "extra_download";
    public static final String SPLASH_FILE_NAME = "splash.srr";

    //动态闪屏序列化地址
    public static final String SPLASH_PATH = BaseApplication.getInstance().getContext().getFilesDir().getAbsolutePath() + "/alpha/splash";

}
