package cn.app.library.widget.floating;

import android.graphics.PathMeasure;

import cn.app.library.widget.floating.effect.ReboundFloatingAnimator;


/**
 * Author UFreedom
 * 
 */
public abstract class BaseFloatingPathAnimator extends ReboundFloatingAnimator implements FloatingPathAnimator {
    
    private PathMeasure pathMeasure;
    private float pos[];

    public float[] getFloatingPosition(float progress) {
        pathMeasure.getPosTan(progress, pos, null);
        return pos;
    }
 
    
    @Override
    public void applyFloatingAnimation(FloatingTextView view) {
        pathMeasure = view.getPathMeasure();
        if (pathMeasure == null) {
            return;
        }
        pos = new float[2];
        applyFloatingPathAnimation(view,0,pathMeasure.getLength());
    }
    
}
