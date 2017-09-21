package cn.app.library.widget.pickerview.lib;

import android.view.GestureDetector;
import android.view.MotionEvent;



final class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {

    final WheelView loopView;

    LoopViewGestureListener(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
