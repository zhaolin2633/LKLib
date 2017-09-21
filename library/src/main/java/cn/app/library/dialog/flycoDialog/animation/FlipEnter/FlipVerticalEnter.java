package cn.app.library.dialog.flycoDialog.animation.FlipEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import cn.app.library.dialog.flycoDialog.animation.BaseAnimatorSet;


public class FlipVerticalEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				// ObjectAnimator.ofFloat(view, "rotationX", -90, 0));
				ObjectAnimator.ofFloat(view, "rotationX", 90, 0));
	}
}
