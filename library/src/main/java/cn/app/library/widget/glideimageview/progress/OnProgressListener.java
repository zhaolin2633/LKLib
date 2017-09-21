package cn.app.library.widget.glideimageview.progress;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by zl on 2017/6/14.
 */
public interface OnProgressListener {

    void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception);
}
