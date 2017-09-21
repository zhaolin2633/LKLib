package cn.app.library.ui.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;

import cn.app.library.R;
import cn.app.library.ui.bigimg.BigImageActivity;


/**
 * <pre>
 * author : zhaolin
 * time : 2017/07/18
 * desc :
 * </pre>
 */
public class WebJavascriptInterface {

    Context context;

    private ArrayList<String> imageUrls;

    public WebJavascriptInterface(Context context, String htmlContent) {
        this.context = context;
        this.imageUrls = WebHtmlUtil.returnImageUrlsFromHtml(htmlContent);
    }

    //查看图片url
    @JavascriptInterface
    public void openImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            Bundle bundle = new Bundle();
            bundle.putInt(BigImageActivity.IMG_POSTION, getUrlPos(url));
            bundle.putStringArrayList(BigImageActivity.IMAGES, imageUrls);
            Intent intent = new Intent(context, BigImageActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.anim_fade_in, 0);
        }
    }

    private int getUrlPos(String url) {
        int pos = 0;
        if (imageUrls != null && imageUrls.size() > 0) {
            for (int i = 0; i < imageUrls.size(); i++) {
                String tempUrl = imageUrls.get(i);
                if (!TextUtils.isEmpty(tempUrl) && tempUrl.equals(url)) {
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }
}
