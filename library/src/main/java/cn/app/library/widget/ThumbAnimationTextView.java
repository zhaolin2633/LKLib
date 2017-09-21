package cn.app.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import cn.app.library.R;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/08/09
 * desc : 点赞加一动画
 * </pre>
 */
public class ThumbAnimationTextView extends android.support.v7.widget.AppCompatTextView implements Animation.AnimationListener {
    Animation mAnimation;

    public ThumbAnimationTextView(Context context) {
        super(context);
        init();
    }

    public ThumbAnimationTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThumbAnimationTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mAnimation = new AnimationUtils().loadAnimation(getContext(), R.anim.thumb_animation_textview);

    }

    public void showAnimation() {
        startAnimation(mAnimation);
        mAnimation.setAnimationListener(this);

    }

    @Override
    public void onAnimationStart(Animation animation) {
        setVisibility(VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        setVisibility(GONE);
        mAnimation.cancel();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
