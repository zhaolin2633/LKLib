package cn.app.library.utils.screenshot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 */
public class BitmapUtil {

	/**
	 * 获取图片文件的信息，是否旋转了90度，如果是则反转
	 * 
	 * @param bitmap
	 *            需要旋转的图片
	 * @param path
	 *            图片的路径
	 */
	public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
		int degree = getPicRotate(path);
		if (degree != 0) {
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree); // 旋转angle度
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
		}
		return bitmap;
	}

	/**
	 * 获取图片文件的信息，是否旋转了，如果是则反转
	 * 
	 * @param inSampleSize
	 *            获取缩略图显示到屏幕上
	 * @param path
	 *            图片的路径
	 * @return Bitmap null为不旋转
	 */
	public static Bitmap reviewPicRotate(int inSampleSize, String path) {
		Bitmap bitmap = null;
		int degree = getPicRotate(path);
		if (degree != 0) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 获取缩略图显示到屏幕上
			opts.inSampleSize = inSampleSize;
			Bitmap cbitmap = BitmapFactory.decodeFile(path, opts);
			if (cbitmap != null) {
				Matrix m = new Matrix();
				int width = cbitmap.getWidth();
				int height = cbitmap.getHeight();
				m.setRotate(degree); // 旋转angle度
				bitmap = Bitmap.createBitmap(cbitmap, 0, 0, width, height, m, true);// 从新生成图片
				return bitmap;
			}
		}
		return bitmap;
	}

	/**
	 * 读取图片文件旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片旋转的角度
	 */
	public static int getPicRotate(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * @author zhaolin
	 * @Desc: TODO 将bitmap转换成String fileName;
	 * @param filePath
	 *            文件夹路径 (例子 SDCard_PATH/Image)
	 * @param bitmap
	 * @return String 文件路径
	 */
	public static String bitmapToFileName(String filePath, Bitmap bitmap) {
		final File f = new File(filePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		String fileName = TimeTool.getTimeName(System.currentTimeMillis()) + ".jpg";

		byte[] photodata = null;

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			// (0 -100)
			bitmap.compress(CompressFormat.JPEG, 80, bos);

			photodata = bos.toByteArray();

			bos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		FileTool.getFile(photodata, filePath, fileName);
		return filePath + "/" + fileName;

	}

	public static String fileText = "file://";

	public static String imageToBase64(String fileName) {
		String attachFile = "";
		String path = null;
		if (!TextUtils.isEmpty(fileName) && fileName.startsWith(fileText)) {
			StringBuffer sb = new StringBuffer(fileName);
			sb.delete(0, fileText.length());
			path = sb.toString();
		} else {
			path = fileName;
		}
		Bitmap mBitmap = ImageResizer.decodeSampledBitmapFromFile(path, 800, 800);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		if (mBitmap != null) {
			mBitmap.compress(CompressFormat.JPEG, 80, outStream);
			byte[] bytes = outStream.toByteArray();
			if (bytes.length > 0) {
				attachFile = Base64.encodeToString(bytes, Base64.DEFAULT);
			}

		}
		return attachFile;
	}

	/**
	 * @param imgPath
	 * @param bitmap
	 * @param imgFormat
	 *            图片格式
	 * @return
	 */
	public static String imgToBase64(String imgPath, Bitmap bitmap, String imgFormat) {
		if (imgPath != null && imgPath.length() > 0) {
			bitmap = readBitmap(imgPath);
		}
		if (bitmap == null) {
			// bitmap not found!!
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, out);

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap scaleBitmap(Bitmap bitmap, int newHeight, int newWidth) {
		Bitmap newBitmap = null;
		if (bitmap != null) {
			int rawHeigh = bitmap.getHeight();
			int rawWidth = bitmap.getHeight();
			// 计算缩放因子
			float heightScale = ((float) newHeight) / rawHeigh;
			float widthScale = ((float) newWidth) / rawWidth;
			// 新建立矩阵
			Matrix matrix = new Matrix();
			matrix.postScale(heightScale, widthScale);
			// 设置图片的旋转角度
			// matrix.postRotate(-30);
			// 设置图片的倾斜
			// matrix.postSkew(0.1f, 0.1f);
			// 将图片大小压缩
			// 压缩后图片的宽和高以及kB大小均会变化
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
		}
		return newBitmap;
	}

	public static Bitmap readBitmap(String imgPath) {
		try {

			String path = null;
			if (!TextUtils.isEmpty(imgPath) && imgPath.startsWith("file://")) {
				StringBuffer sb = new StringBuffer(imgPath);
				sb.delete(0, 7);
				path = sb.toString();
			} else {
				path = imgPath;
			}
			return BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param base64Data
	 * @param imgName
	 * @param imgFormat
	 *            图片格式
	 */
	public static void base64ToBitmap(String base64Data, String imgName, String imgFormat) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		File myCaptureFile = new File("/sdcard/", imgName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myCaptureFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		boolean isTu = bitmap.compress(CompressFormat.JPEG, 100, fos);
		if (isTu) {
			// fos.notifyAll();
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 把Bitmap转换成Base64
	public static String getBitmapStrBase64(Bitmap bitmap) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 80, baos);
			byte[] bytes = baos.toByteArray();
			return Base64.encodeToString(bytes, 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";

	}

	// 把Base64转换成Bitmap
	public static Bitmap getBitmap(String iconBase64) {
		byte[] bitmapArray;
		bitmapArray = Base64.decode(iconBase64, 0);
		return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}

	/**
	 * 把bitmap转成成byte数组
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 80, bos);
		return bos.toByteArray();
	}

	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/** 文件转化为字节数组 */
	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	@SuppressWarnings("resource")
	public static byte[] uriToByte(Uri uri, Context context) {
		byte[] bytes = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
		if (actualimagecursor == null)
			return bytes;
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor.getString(actual_image_column_index);

		try {
			File f = new File(img_path);
			FileInputStream fis = new FileInputStream(f);
			bytes = new byte[fis.available()];
			BufferedInputStream bops = new BufferedInputStream(fis);
			bops.read(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * uri 转成 bitmap
	 * 
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static Bitmap uriToBitmap(Activity activity, Uri uri) {
		Bitmap bitmap = null;
		if (uri == null) {
			return bitmap;
		}
		try {
			return bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * bimap 转成uri
	 * 
	 * @param activity
	 * @param bitmap
	 * @return
	 */
	public static Uri bitmapToUri(Activity activity, Bitmap bitmap) {
		Uri uri = null;
		if (bitmap == null) {
			return uri;
		}
		return uri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));
	}

	public static File saveImageToPath(Activity activity, Bitmap bitmap, String url) {
		if (bitmap == null)
			return null;
		if (TextUtils.isEmpty(url))
			return null;
		if (url.endsWith(".jpg") || url.endsWith(".png")|| url.endsWith(".jpeg")) {
			try {
				String imageName = url.substring(url.lastIndexOf("/") + 1,url.lastIndexOf("."));
				File file = new File(CommonUtils.getPhotoCachePic(activity) + "/" + imageName);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static Uri saveImageToPath2(Activity context, String url) {
		Uri imageFilePath = null;
		if (TextUtils.isEmpty(url))
			return imageFilePath;
		if (url.endsWith(".jpg") || url.endsWith(".png")|| url.endsWith(".jpeg")) {
			String imageName = url.substring(url.lastIndexOf("/") + 1);
			String status = Environment.getExternalStorageState();
			ContentValues values = new ContentValues(3);
			values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
			values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
				imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
			}
		}
		return imageFilePath;
	}

}
