package cn.app.library.listener;

import android.view.View;

/**
 * Author：xiaohaibin
 * Time：2017/6/6
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：处理按钮重复点击
 */
public abstract  class OnDoubleClickListener implements View.OnClickListener{

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        }
    }
}
