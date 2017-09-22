package cn.app.library.dialog.picker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import cn.app.library.R;
import cn.app.library.dialog.flycoDialog.dialog.listener.OnOperItemClickL;
import cn.app.library.dialog.flycoDialog.dialog.widget.ActionSheetDialog;
import cn.app.library.dialog.picker.activity.PickImageActivity;
import cn.app.library.dialog.picker.storage.StorageType;
import cn.app.library.dialog.picker.storage.StorageUtil;
import cn.app.library.dialog.picker.util.StringUtil;


/**
 * Created by huangjun on 2015/9/22.
 */
public class PickImageHelper {
    public static final int PICK_AVATAR_REQUEST = 0x003;

    public static class PickImageOption {
        /**
         * 图片选择器标题
         */
        public int titleResId = R.string.choose;

        /**
         * 是否多选
         */
        public boolean multiSelect = true;

        /**
         * 最多选多少张图（多选时有效）
         */
        public int multiSelectMaxCount = 9;

        /**
         * 是否进行图片裁剪(图片选择模式：false / 图片裁剪模式：true)
         */
        public boolean crop = false;

        /**
         * 图片裁剪的宽度（裁剪模式时有效）
         */
        public int cropOutputImageWidth = 720;

        /**
         * 图片裁剪的高度（裁剪模式时有效）
         */
        public int cropOutputImageHeight = 720;

        /**
         * 图片选择保存路径
         */
        public String outputPath = StorageUtil.getWritePath(StringUtil.get32UUID() + ".jpg", StorageType.TYPE_TEMP);
    }

    /**
     * 打开图片选择器
     */
    public static void pickImage(final Context context, final int requestCode, final PickImageOption option) {
        if (context == null) {
            return;
        }
        final String[] stringItems = {"拍照", "从手机相册选择"};
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
        dialog.title(context.getString(option.titleResId))//
                .titleTextSize_SP(14.5f)//
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    int from = PickImageActivity.FROM_CAMERA;
                    if (!option.crop) {
                        PickImageActivity.start((Activity) context, requestCode, from, option.outputPath, option.multiSelect, 1,
                                true, false, 0, 0);
                    } else {
                        PickImageActivity.start((Activity) context, requestCode, from, option.outputPath, false, 1,
                                false, true, option.cropOutputImageWidth, option.cropOutputImageHeight);
                    }
                } else if (position == 1) {
                    int from = PickImageActivity.FROM_LOCAL;
                    if (!option.crop) {
                        PickImageActivity.start((Activity) context, requestCode, from, option.outputPath, option.multiSelect,
                                option.multiSelectMaxCount, true, false, 0, 0);
                    } else {
                        PickImageActivity.start((Activity) context, requestCode, from, option.outputPath, false, 1,
                                false, true, option.cropOutputImageWidth, option.cropOutputImageHeight);
                    }
                }
                dialog.dismiss();
            }
        });
    }
}
