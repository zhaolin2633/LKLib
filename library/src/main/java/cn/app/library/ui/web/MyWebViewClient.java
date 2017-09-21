package cn.app.library.ui.web;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;



/**
 * <pre>
 * author : zhaolin
 * time : 2017/07/18
 * desc :
 * </pre>
 */
public class MyWebViewClient extends WebViewClient {


    @Override
    public void onPageFinished(WebView view, String url) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageFinished(view, url);
        // html加载完成之后，添加监听图片的点击js函数
        WebHtmlUtil.clickImage(view);
        
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        try {
//            Uri uri = Uri.parse(url); // url为你要链接的地址
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            view.getContext().startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            WebViewFragment.openUrl(view.getContext(), "",url, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
    }
}
