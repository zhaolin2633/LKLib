package cn.app.library.widget.toast;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import cn.app.library.R;

/**
 * Author:Tiger
 * <p>
 * CrateTime:2016-12-16 9:27
 * <p>
 * Description:Toast工具
 */
public class ToastTextUtil {

    private volatile static ToastTextUtil instance;

    /**
     * @param charSequence 显示文字
     */
    public void show(CharSequence charSequence) {
        show(charSequence, Toast.LENGTH_SHORT);
    }

    public Context mContent;

    public ToastTextUtil(Context context) {
        this.mContent = context;
        init(context);
    }

    public static ToastTextUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (ToastTextUtil.class) {
                if (instance == null) {
                    instance = new ToastTextUtil(context);
                }
            }
        }
        return instance;
    }

    private TextView contentTextView;
    private Toast toast;

    public void init(Context context) {
        contentTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.view_toast, null);
        toast = new Toast(context);
        toast.setView(contentTextView);
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    /**
     * @param charSequence 显示文字
     * @param duration     显示时长 Toast.LENGTH_LONG/LENGTH_SHORT
     */
    public void show(CharSequence charSequence, int duration) {
        if (!TextUtils.isEmpty(charSequence)) {
            contentTextView.setText(charSequence);
            toast.setDuration(duration);
            toast.show();
        }
    }
}
