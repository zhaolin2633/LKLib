package cn.app.library.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.app.library.http.config.Constants;
import cn.app.library.utils.DownLoadImageUtils;
import cn.app.library.utils.SerializableUtils;

import static cn.app.library.utils.SerializableUtils.readObject;


public class SplashDownLoadService extends IntentService {

    private AdEntity mScreen;
    private static final String SPLASH_FILE_NAME = "splash.srr";

    public SplashDownLoadService() {
        super("SplashDownLoad");
    }

    public static void startDownLoadSplashImage(Context context, String action) {
        Intent intent = new Intent(context, SplashDownLoadService.class);
        intent.putExtra(Constants.EXTRA_DOWNLOAD, action);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getStringExtra(Constants.EXTRA_DOWNLOAD);
            if (action.equals(Constants.DOWNLOAD_SPLASH)) {
                loadSplashNetDate();
            }
        }
    }

    private void loadSplashNetDate() {
//        HttpManager.getInstance(getBaseContext())
//                .createService(BaseApiService.class)
//                .getStartAdImage()
//                .compose(TransformUtils.<HttpResult<AdEntity>>defaultSchedulers())
//                .subscribe(new HttpResultSubscriber<AdEntity>() {
//                    @Override
//                    public void onSuccess(AdEntity adEntity) {
//                        mScreen = adEntity;
//                        AdEntity splashLocal = getSplashLocal();
//                        if (mScreen != null && mScreen.pic != null && !TextUtils.isEmpty(mScreen.pic)) {
//                            if (splashLocal == null) {
//                                startDownLoadSplash(Constants.SPLASH_PATH, mScreen.pic);
//                            } else if (isNeedDownLoad(splashLocal.pic, mScreen.pic)) {
//                                startDownLoadSplash(Constants.SPLASH_PATH, mScreen.pic);
//                            }
//                        } else {
//                            if (splashLocal != null) {
//                                File splashFile = null;
//                                try {
//                                    splashFile = SerializableUtils.getSerializableFile(Constants.SPLASH_PATH, Constants.SPLASH_FILE_NAME);
//                                    if (splashFile.exists()) {
//                                        splashFile.delete();
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void _onError(String msg, int code) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
    }

    private AdEntity getSplashLocal() {
        AdEntity splash = null;
        try {
            File splashFile = SerializableUtils.getSerializableFile(Constants.SPLASH_PATH, SPLASH_FILE_NAME);
            splash = (AdEntity) readObject(splashFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return splash;
    }

    /**
     * @param path 本地存储的图片绝对路径
     * @param url  网络获取url
     * @return 比较储存的 图片名称的哈希值与 网络获取的哈希值是否相同
     */
    private boolean isNeedDownLoad(String path, String url) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (getImageName(path).hashCode() != getImageName(url).hashCode()) {
            return true;
        }
        return false;
    }


    private String getImageName(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String[] split = url.split("/");
        String nameWith_ = split[split.length - 1];
        String[] split1 = nameWith_.split("\\.");
        return split1[0];
    }

    private void startDownLoadSplash(String splashPath, String burl) {
        DownLoadImageUtils.downLoad(splashPath, new DownLoadImageUtils.DownLoadInterFace() {
            @Override
            public void afterDownLoad(ArrayList<String> savePaths) {
                if (savePaths.size() == 1) {
                    if (mScreen != null && mScreen.pic != null && !TextUtils.isEmpty(mScreen.pic)) {
                        mScreen.pic = savePaths.get(0);
                    }
                    SerializableUtils.writeObject(mScreen, Constants.SPLASH_PATH + "/" + SPLASH_FILE_NAME);
                } else {
                    Logger.i("Splash", "闪屏页面下载失败" + savePaths);
                }
            }
        }, burl);
    }

}
