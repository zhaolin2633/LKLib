package cn.app.library.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import cn.app.library.R;
import cn.app.library.base.BaseDialogFragment;
import cn.app.library.picture.lib.permissions.RxPermissions;
import cn.app.library.utils.ScreenUtil;
import cn.app.library.widget.toast.ToastUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Description:统一选择图片对话框
 */
public class ChooseImageDialog extends BaseDialogFragment implements View.OnClickListener {

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
        findView(R.id.choose_image_take).setOnClickListener(this);
        findView(R.id.choose_image_system).setOnClickListener(this);
        findView(R.id.choose_image_cancel).setOnClickListener(this);
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
                        ToastUtil.show("选取图片失败，请重试！");
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.choose_image_take) {
            // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
            RxPermissions permissions = new RxPermissions(getActivity());
            permissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, parUri(tempFile));
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);
                            return;
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });

        } else if (i == R.id.choose_image_system) {
            // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
            RxPermissions permissions = new RxPermissions(getActivity());
            permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(picture, REQUEST_ALBUM);
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });

            return;
        } else if (i == R.id.choose_image_cancel) {
            dismiss();
            return;
        }
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public interface Operator {

        void onGetImage(String path);

    }


    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getContext().getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(getContext(), authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }


}
