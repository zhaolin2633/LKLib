package cn.app.library.ui.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.app.library.R;
import cn.app.library.base.BaseNormalFragment;
import cn.app.library.base.OnKeyDownListener;
import cn.app.library.http.config.Constants;
import cn.app.library.utils.DeviceUuidFactory;
import cn.app.library.utils.JumpUtil;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.utils.SignatureUtil;
import cn.app.library.widget.toast.ToastUtil;


/**
 * Description:网页加载器
 */
public class WebViewFragment extends BaseNormalFragment implements View.OnClickListener {

    private TextView mTvTitle;
    private ImageView mIvBack;
    private ImageView mIvClose;
    private ImageView mIvShare;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private boolean isShare = false;//是否可以分享
    private boolean isClose = true;//是否有关闭按钮
    private String currentUrl = "";

    /**
     * 打开网页链接
     */
    public static void openUrl(Context context, String title, String url, String shareContent) {
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", WebViewFragment.DATA_TYPE_URL);
        bundle.putString("data", url);
        bundle.putString("title", title);
        bundle.putString("share_content", shareContent);
        bundle.putBoolean("is_share", true);
        JumpUtil.jump(context, WebViewFragment.class.getName(), title, bundle);
    }

    /**
     * 打开网页链接
     */
    public static void openUrl(Context context, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", WebViewFragment.DATA_TYPE_URL);
        bundle.putString("data", url);
        bundle.putString("title", title);
        JumpUtil.jump(context, WebViewFragment.class.getName(), title, bundle);
    }

    /**
     * 打开App内部协议类的连接
     */
    public static void openAppUrl(Context context, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", WebViewFragment.DATA_TYPE_URL);
        bundle.putString("data", url);
        bundle.putString("title", title);
        bundle.putBoolean("is_close", false);
        JumpUtil.jump(context, WebViewFragment.class.getName(), title, bundle);
    }

    /**
     * 打开网页链接
     */
    public static void openUrl(Context context, String title, String url, boolean isClose, String shareContent, String image) {
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", WebViewFragment.DATA_TYPE_URL);
        bundle.putString("data", url);
        bundle.putString("title", title);
        bundle.putString("share_content", shareContent);
        bundle.putBoolean("is_share", true);
        bundle.putBoolean("is_close", isClose);
        bundle.putString("image", image);
        JumpUtil.jump(context, WebViewFragment.class.getName(), title, bundle);
    }

    /**
     * 显示HTML代码
     */
    public static void openHtml(Context context, String title, String html) {
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", WebViewFragment.DATA_TYPE_HTML);
        bundle.putString("data", html);
        bundle.putString("title", title);
        JumpUtil.jump(context, WebViewFragment.class.getName(), title, bundle);
    }

    public final static int DATA_TYPE_URL = 1;
    public final static int DATA_TYPE_HTML = 2;

    private WebView webView;
    private ProgressBar progressBar;

    private int dataType;
    private String data;
    private String title;
    private String imageUrl;
    private String share_content;
    private boolean isHtml = false;
    private boolean finished = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web);
        init();
        findViews();
        initWebView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getContainerActivity().hideNavigationBar();
        mIvBack.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
    }

    @Override
    protected void registerViews() {
        getContainerActivity().setOnKeyDownListener(new OnKeyDownListener() {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        goBack();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void goBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void findViews() {
        mTvTitle = findView(R.id.tv_title);
        mIvBack = findView(R.id.iv_back);
        mIvClose = findView(R.id.iv_close);
        mIvShare = findView(R.id.iv_share);
        if (isClose) {
            mIvClose.setVisibility(View.VISIBLE);
            mTvTitle.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            mIvClose.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(ScreenUtil.dip2px(getContainerActivity(), 48), 0, ScreenUtil.dip2px(getContainerActivity(), 48), 0);
            mTvTitle.setLayoutParams(layoutParams);
            mTvTitle.setGravity(Gravity.CENTER);
        }
        registerViews();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = findView(R.id.web_webview);
        progressBar = findView(R.id.web_progress);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("mailto:")
                        || url.startsWith("geo:") || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(intent);
                    return true;
                }
                currentUrl = url;
                view.loadUrl(url, getMapHeader());
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                    mIvShare.setVisibility(isShare ? View.VISIBLE : View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!isHtml && !finished) {
                    mTvTitle.setText(title);
                }
            }

            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    ToastUtil.show("不能打开文件选择器");
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

        });
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(true);
        //设置webview的浏览器标识 User-Agent
        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + ";txf_app");
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        // 解决HTTPS协议下出现的mixed content问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //隐藏缩放控件
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getActivity().getCacheDir().getPath());
        settings.setAppCacheEnabled(true);

        showWebdata();
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("dataType")) {
                dataType = bundle.getInt("dataType");
            }
            if (bundle.containsKey("data")) {
                data = bundle.getString("data");
            }
            if (bundle.containsKey("title")) {
                title = bundle.getString("title");
            }
            if (bundle.containsKey("share_content")) {
                share_content = bundle.getString("share_content");
            }
            if (bundle.containsKey("is_share")) {
                isShare = bundle.getBoolean("is_share");
            }
            if (bundle.containsKey("is_close")) {
                isClose = bundle.getBoolean("is_close");
            }
            if (bundle.containsKey("image")) {
                imageUrl = bundle.getString("image");
            }
        }
    }

    private void showWebdata() {
        switch (dataType) {
            case DATA_TYPE_URL:
                currentUrl = data;
                webView.loadUrl(data, getMapHeader());
                break;
            case DATA_TYPE_HTML:
                webView.loadData(data, "text/html; charset=utf-8", "utf-8");
                isHtml = true;
                break;
        }
    }

    public Map<String, String> getMapHeader() {
        final long currentTimeMillis = System.currentTimeMillis();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("key-random", Constants.APP_RANDOM);
        hashMap.put("key-time", String.valueOf(currentTimeMillis));
        hashMap.put("key-id", Constants.KEY_ID);
        hashMap.put("app-id", new DeviceUuidFactory(getContext()).getDeviceUuid().toString());
        hashMap.put("key-signature", SignatureUtil.NoSignUtil(Constants.APP_RANDOM, String.valueOf(currentTimeMillis)));
        return hashMap;
    }

    private void finish() {
        finished = true;
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            goBack();
            return;

        } else if (i == R.id.iv_close) {
            finish();
            return;
        } else if (i == R.id.iv_share) {

            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != getActivity().RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            ToastUtil.show("不能打开文件选择器");
    }

}
