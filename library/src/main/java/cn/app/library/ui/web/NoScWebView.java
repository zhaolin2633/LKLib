package cn.app.library.ui.web;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/06/21
 * desc :
 * </pre>
 */
public class NoScWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;
    public EditText mFocusDistraction;
    public Context mContext;
    public NoScWebView(Context context) {
        super(context);
        init(context);
    }

    public NoScWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoScWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public NoScWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
        init(context);
    }

    public void init(Context context) {
        // This lets the layout editor display the view.
        if (isInEditMode()) return;

        mContext = context;

        mFocusDistraction = new EditText(context);
        mFocusDistraction.setBackgroundResource(android.R.color.transparent);
        this.addView(mFocusDistraction);
        mFocusDistraction.getLayoutParams().width = 1;
        mFocusDistraction.getLayoutParams().height = 1;
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                int maxOverScrollY, boolean isTouchEvent) {
        return false;
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    /**
     * 使WebView不可滚动
     * */
    @Override
    public void scrollTo(int x, int y){
        super.scrollTo(0,0);
    }
    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }
}
