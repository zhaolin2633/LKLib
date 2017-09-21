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
            R.mipmap.sprite_2130002_007,
            R.mipmap.sprite_2130002_008,
            R.mipmap.sprite_2130002_009,
            R.mipmap.sprite_2130002_010,
            R.mipmap.sprite_2130002_011,
            R.mipmap.sprite_2130002_012,
            R.mipmap.sprite_2130002_013,
            R.mipmap.sprite_2130002_014,
            R.mipmap.sprite_2130002_015,
            R.mipmap.sprite_2130002_016,
            R.mipmap.sprite_2130002_017,
            R.mipmap.sprite_2130002_018,
            R.mipmap.sprite_2130002_019,
            R.mipmap.sprite_2130002_020,
            R.mipmap.sprite_2130002_021,
            R.mipmap.sprite_2130002_022,
            R.mipmap.sprite_2130002_023,
            R.mipmap.sprite_2130002_024,
            R.mipmap.sprite_2130002_025,
            R.mipmap.sprite_2130002_026,
            R.mipmap.sprite_2130002_027,
            R.mipmap.sprite_2130002_028,
            R.mipmap.sprite_2130002_029,
            R.mipmap.sprite_2130002_030,
            R.mipmap.sprite_2130002_031,
            R.mipmap.sprite_2130002_032,
            R.mipmap.sprite_2130002_033,
            R.mipmap.sprite_2130002_034,
            R.mipmap.sprite_2130002_035,
            R.mipmap.sprite_2130002_036,
            R.mipmap.sprite_2130002_037,
            R.mipmap.sprite_2130002_038,
            R.mipmap.sprite_2130002_039,
            R.mipmap.sprite_2130002_040,
            R.mipmap.sprite_2130002_041,
            R.mipmap.sprite_2130002_042,
            R.mipmap.sprite_2130002_043,
            R.mipmap.sprite_2130002_044,
            R.mipmap.sprite_2130002_045,
            R.mipmap.sprite_2130002_046,
            R.mipmap.sprite_2130002_047,
            R.mipmap.sprite_2130002_048,
            R.mipmap.sprite_2130002_049,
            R.mipmap.sprite_2130002_050,
            R.mipmap.sprite_2130002_051,
            R.mipmap.sprite_2130002_052,
            R.mipmap.sprite_2130002_053,
            R.mipmap.sprite_2130002_054,
            R.mipmap.sprite_2130002_055,
            R.mipmap.sprite_2130002_056,
            R.mipmap.sprite_2130002_057,
            R.mipmap.sprite_2130002_058,
            R.mipmap.sprite_2130002_059,
            R.mipmap.sprite_2130002_060,
            R.mipmap.sprite_2130002_061,
            R.mipmap.sprite_2130002_062,
            R.mipmap.sprite_2130002_063,
            R.mipmap.sprite_2130002_064,
            R.mipmap.sprite_2130002_065,
            R.mipmap.sprite_2130002_066,
            R.mipmap.sprite_2130002_067,
            R.mipmap.sprite_2130002_068,
            R.mipmap.sprite_2130002_069,
            R.mipmap.sprite_2130002_070,
            R.mipmap.sprite_2130002_071,
            R.mipmap.sprite_2130002_072,
            R.mipmap.sprite_2130002_073,
            R.mipmap.sprite_2130002_074,
            R.mipmap.sprite_2130002_075,
            R.mipmap.sprite_2130002_076,
            R.mipmap.sprite_2130002_077,
            R.mipmap.sprite_2130002_078,
            R.mipmap.sprite_2130002_079,
            R.mipmap.sprite_2130002_080,
            R.mipmap.sprite_2130002_081,
            R.mipmap.sprite_2130002_082,
            R.mipmap.sprite_2130002_083,
            R.mipmap.sprite_2130002_084,
            R.mipmap.sprite_2130002_085,
            R.mipmap.sprite_2130002_086,
            R.mipmap.sprite_2130002_087,
            R.mipmap.sprite_2130002_088,
            R.mipmap.sprite_2130002_089,
            R.mipmap.sprite_2130002_090,
            R.mipmap.sprite_2130002_091,
            R.mipmap.sprite_2130002_092,
            R.mipmap.sprite_2130002_093,
            R.mipmap.sprite_2130002_094,
            R.mipmap.sprite_2130002_095,
            R.mipmap.sprite_2130002_096,
            R.mipmap.sprite_2130002_097,
            R.mipmap.sprite_2130002_098,
            R.mipmap.sprite_2130002_099,
            R.mipmap.sprite_2130002_100};
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
