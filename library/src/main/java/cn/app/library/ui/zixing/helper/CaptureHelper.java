package cn.app.library.ui.zixing.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.app.library.ui.zixing.CaptureActivity;
import cn.app.library.utils.AES;
import cn.app.library.widget.toast.ToastUtil;


/**
 * <pre>
 * author : zhaolin
 * time : 2017/05/04
 * desc :二维码扫描管理类
 * </pre>
 */
public class CaptureHelper {
    /**
     * 启动扫描二维码activity 返回的结果处理
     *
     * @param activity
     * @param requestCode
     * @param intent
     */
    public static void onActivityResult(final Activity activity, int requestCode, Intent intent) {
        if (requestCode == CaptureActivity.INTENT_REQUEST_CODE_CAPTURE) {
            if (intent == null)
                return;
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String contents = bundle.getString(CodeUtils.RESULT_STRING);//图像内容
                try {
                    if (TextUtils.isEmpty(contents))
                        return;
                    contents = AES.decrypt2(contents);
                    if (!contents.contains(",") || contents.split(",").length <= 1) {
                        ToastUtil.show("暂不支持此二维码的识别");
                        return;
                    }
                    String contentTag = contents.split(",")[0];
                    String id = contents.split(",")[1];
                    if (!TextUtils.isEmpty(contentTag) && contentTag.startsWith("team")) {
                        jionTeam(activity, id);
                    } else if (!TextUtils.isEmpty(contentTag) && contentTag.startsWith("user")) {
                        jionUser(activity, id);
                    } else {
                        ToastUtil.show("暂不支持此二维码的识别");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加入群主
     *
     * @param activity
     * @param contents
     */
    private static void jionTeam(final Activity activity, final String contents) {


    }

    /**
     * 加好友
     *
     * @param activity
     * @param contents
     */
    private static void jionUser(final Activity activity, String contents) {


    }
}
