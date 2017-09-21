package cn.app.library.utils;

/**
 * Describe：图片分辨率处理工具类
 */
public class ImageUtils {

    /**
     * 获取小图url
     *
     * @param imgUrl
     * @return
     */
    public static String getSmallImageUrl(String imgUrl) {
        return imgUrl + "?imageView2/2/w/200/h/200";
    }

    /**
     * 获取大图url
     *
     * @param imgUrl
     * @return
     */
    public static String getBigImageUrl(String imgUrl) {
        return imgUrl + "?imageView2/2/w/1000/h/1000";
    }

    /**
     * 自定义图片宽高url
     *
     * @param imgUrl
     * @return
     */
    public static String getNormalImageUrl(String imgUrl, int widthAndHeigth) {
        return imgUrl + "?imageView2/2/w/" + widthAndHeigth + "/h/" + widthAndHeigth;
    }

}
