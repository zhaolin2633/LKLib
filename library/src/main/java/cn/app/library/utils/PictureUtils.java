package cn.app.library.utils;

import android.app.Activity;
import android.content.Context;



import java.io.File;

import cn.app.library.R;
import cn.app.library.picture.lib.PictureSelector;
import cn.app.library.picture.lib.compress.Luban;
import cn.app.library.picture.lib.compress.OnCompressListener;
import cn.app.library.picture.lib.config.PictureConfig;
import cn.app.library.picture.lib.config.PictureMimeType;


/**
 * Author：xiaohaibin
 * Time：2017/6/8
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：多图选择框架
 */
public class PictureUtils {

    private static PictureUtils sPickPhotoUtils;

    public static PictureUtils getInstance() {
        if (sPickPhotoUtils == null) {
            synchronized (PictureUtils.class) {
                if (sPickPhotoUtils == null) {
                    sPickPhotoUtils = new PictureUtils();
                }
            }
        }
        return sPickPhotoUtils;
    }

    public void init(Activity activity,int maxSelectNum,int themeStyleId) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .theme(themeStyleId)// 主题样式设置
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(false)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)// 鲁班压缩
                .glideOverride(160, 160)
                .isGif(false)// 是否显示gif图片
                .openClickSound(false)// 是否开启点击声音
                .compressGrade(Luban.CUSTOM_GEAR)
                .compressMaxKB(1024)//压缩最大值kb
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调
    }

    /**
     * 压缩单张图片
     * @param context
     * @param path
     * @param compressListener
     */
    public void CompressImage(Context context, String path, OnCompressListener compressListener) {
        Luban.compress(context, new File(path))
                .setMaxSize(1024)
                .setMaxHeight(1920)
                .setMaxWidth(1920)
                .launch(compressListener);
    }


}
