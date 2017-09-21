package cn.app.library.ui.web.html_textview;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.app.library.R;


/**
 * ImageLoader加载图片的配置类
 */
public class ImageOptHelper {

    public static DisplayImageOptions getImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.loading_default)
                .showImageForEmptyUri(R.drawable.loading_default)
                .showImageOnFail(R.drawable.loading_default)
                .build();
        return imgOptions;
    }

    public static DisplayImageOptions getBigImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return imgOptions;
    }

    public static DisplayImageOptions getAvatarOptions() {
        DisplayImageOptions avatarOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.loading_default)
                .showImageForEmptyUri(R.drawable.loading_default)
                .showImageOnFail(R.drawable.loading_default)
                .build();
        return avatarOptions;
    }

    public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.loading_default)
                .showImageForEmptyUri(R.drawable.loading_default)
                .showImageOnFail(R.drawable.loading_default)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
        return options;
    }
}
