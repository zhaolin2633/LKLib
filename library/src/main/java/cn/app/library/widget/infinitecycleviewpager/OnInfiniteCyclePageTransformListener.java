package cn.app.library.widget.infinitecycleviewpager;

import android.view.View;

/**
 * Created by GIGAMOLE on 7/27/16.
 */
public interface OnInfiniteCyclePageTransformListener {
    void onPreTransform(final View page, final float position);

    void onPostTransform(final View page, final float position);
}