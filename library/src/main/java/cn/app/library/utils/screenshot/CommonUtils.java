package cn.app.library.utils.screenshot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Environment;

import java.io.File;

public class CommonUtils {
	public final static String cacheMainPath = getSDPath() + File.separator;

	@SuppressLint("SdCardPath")
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return "/sdcard";
		}
		return sdDir.toString();
	}

	public static String getPackageName(Activity app) {
		return app.getPackageName();
	}

	public static String getPhotoCachePic(Activity app) {
		return cacheMainPath + getPackageName(app) + "/Image";
	}
	public static String getCacheApk(Activity app) {
		return cacheMainPath + getPackageName(app) + "/UpdateApk";
	}

}
