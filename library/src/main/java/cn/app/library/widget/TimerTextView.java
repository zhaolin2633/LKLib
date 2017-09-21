package cn.app.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jxnk25 on 2016/12/30.
 * @description： 倒计时控件
 */
@SuppressLint("AppCompatCustomView")
public class TimerTextView extends TextView {

    private int remainCount = 0;
    private boolean isSale;

    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (refreshTime()) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    };

    public void setData(int remainCount, boolean isSale) {
        this.remainCount = remainCount;
        this.isSale = isSale;
        handler.sendEmptyMessage(0);
    }

    private boolean refreshTime() {
        int d = 0;
        int h = 0;
        int m = 0;
        String text = "";
        remainCount--;
        if (remainCount <= 0) {
            //已下架
            setText(isSale ? "已下架" : "已停止");
            return false;
        } else {
            text += "还剩";
            d = remainCount / (24 * 60 * 60);
            h = (remainCount - 24 * 60 * 60 * d) / (60 * 60);
            m = (remainCount - 24 * 60 * 60 * d - 60 * 60 * h) / 60;
            text += d;
            text += "天";
            if (h < 10) {
                text += "0" + String.valueOf(h);
            } else {
                text += String.valueOf(h);
            }
            text += "时";
            if (m < 10) {
                text += "0" + String.valueOf(m);
            } else {
                text += String.valueOf(m);
            }
            text += "分";

            text += (isSale ? "下架" : "停止");

            setText(text);
        }
        return true;
    }


}
