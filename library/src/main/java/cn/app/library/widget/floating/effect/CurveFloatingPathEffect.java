package cn.app.library.widget.floating.effect;

import android.graphics.Path;

import cn.app.library.widget.floating.FloatingPath;
import cn.app.library.widget.floating.FloatingPathEffect;
import cn.app.library.widget.floating.FloatingTextView;


/**
 * Author UFreedom
 */
public class CurveFloatingPathEffect implements FloatingPathEffect {


    @Override
    public FloatingPath getFloatingPath(FloatingTextView floatingTextView) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.quadTo(-100, -200, 0, -300);
        path.quadTo(200, -400, 0, -500);
        return FloatingPath.create(path, false);
    }

}
