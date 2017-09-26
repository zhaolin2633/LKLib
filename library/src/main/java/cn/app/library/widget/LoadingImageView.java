package cn.app.library.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.app.library.R;


/**
 * 作者：Tiger
 * <p>
 * 时间：2016-08-15 9:23
 * <p>
 * 描述：帧动画
 */

@SuppressLint("AppCompatCustomView")
public class LoadingImageView extends ImageView {

    private final int[] loadingImages = {
            R.mipmap.spinner_0,
            R.mipmap.spinner_1,
            R.mipmap.spinner_2,
            R.mipmap.spinner_3,
            R.mipmap.spinner_4,
            R.mipmap.spinner_5,
            R.mipmap.spinner_6,
            R.mipmap.spinner_7,
            R.mipmap.spinner_8,
            R.mipmap.spinner_9,
            R.mipmap.spinner_10,
            R.mipmap.spinner_11};
    private ValueAnimator animator;

    public LoadingImageView(Context context) {
        super(context);
        init();
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        animator = ValueAnimator.ofInt(0, loadingImages.length - 1);
        animator.setDuration(loadingImages.length * 40);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setImageResource(loadingImages[(int) animation.getAnimatedValue()]);
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
    }

    public void start() {
        if (animator != null) {
            animator.start();
        }
    }

    public void stop() {
        if (animator != null) {
            animator.cancel();
        }
    }

}
