package cn.app.library.dialog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import cn.app.library.R;
import cn.app.library.base.BaseDialogFragment;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.widget.toast.ToastCustomUtils;


/**
 * Author:xiaohaibin
 * <p>
 * CrateTime:2016-12-20 10:30:07
 * <p>
 * Description:统一选择图片对话框
 */
public class ChooseImageDialog extends BaseDialogFragment {

    private final static int REQUEST_CAMERA = 0x123;
    private final static int REQUEST_ALBUM = 0x124;
    private File tempFile;

    private Operator operator;

    public static ChooseImageDialog newInstance() {
        Bundle args = new Bundle();
        ChooseImageDialog fragment = new ChooseImageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.dialog_choose_image);
        findView(R.id.choose_image_take).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });
        findView(R.id.choose_image_system).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, REQUEST_ALBUM);
            }
        });
        findView(R.id.choose_image_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    if (operator != null) {
                        operator.onGetImage(tempFile.getPath());
                        dismiss();
                    }
                    break;
                case REQUEST_ALBUM:
                    if (!getResultFromAlbum(data)) {
                        ToastCustomUtils.showShort(getContext(), "选取图片失败，请重试！");
                    }
                    break;
            }
        }
    }

    /**
     * 处理相册图片返回结果
     */
    private boolean getResultFromAlbum(Intent data) {
        if (data == null) {
            return false;
        }
        Uri selectedImage = data.getData();
        if (selectedImage == null) {
            return false;
        }
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        if (c == null) {
            return false;
        }
        if (!c.moveToFirst()) {
            return false;
        }
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        if (operator != null) {
            operator.onGetImage(picturePath);
            dismiss();
        }
        return true;
    }

    private void init() {
        final String fileName = System.currentTimeMillis() + "";
        if (getActivity().getExternalCacheDir() == null) {
            tempFile = new File(getActivity().getCacheDir().getPath(), fileName);
        } else {
            tempFile = new File(getActivity().getExternalCacheDir().getPath(), fileName);
        }
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return new ViewGroup.LayoutParams(ScreenUtil.getScreenWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public interface Operator {

        void onGetImage(String path);

    }

}
