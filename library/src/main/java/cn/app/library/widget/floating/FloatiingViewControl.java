package cn.app.library.widget.floating;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import cn.app.library.widget.floating.effect.CurveFloatingPathEffect;
import cn.app.library.widget.floating.effect.CurvePathFloatingAnimator;
import cn.app.library.widget.floating.effect.ScaleFloatingAnimator;

/**
 * <pre>
 * author : zhaolin
 * time : 2017/08/09
 * desc :点击按钮悬浮动画
 * FloatingText   floatingText = new FloatingText.FloatingTextBuilder(Activity)
 * textColor(Color.RED) // 漂浮字体的颜色
 * textSize(100)   // 浮字体的大小
 * textContent("+1000") // 浮字体的内容
 * offsetX(100) // FloatingText 相对其所贴附View的水平位移偏移量
 * offsetY(100) // FloatingText 相对其所贴附View的垂直位移偏移量
 * floatingAnimatorEffect(FloatingAnimator) // 漂浮动画
 * floatingPathEffect(FloatingPathEffect) // 漂浮的路径
 * build();
 * floatingText.attach2Window(); //将FloatingText贴附在Window上
 * //启动漂浮效果
 * floatingText.startFloating(View); // 传入一个View，FloatingText 就会相对于这个View执行漂浮效果
 * </pre>
 */
public class FloatiingViewControl {
    /**
     * 向上直线效果
     *
     * @param activity
     * @param floatText
     * @param view
     */
    public static void verticalFloating(Activity activity, String floatText, View view) {
        final FloatingText translateFloatingText = new FloatingText.FloatingTextBuilder(activity)
                .textColor(Color.RED) // 漂浮字体的颜色
                .textSize(60) // 浮字体的大小
                .textContent(!TextUtils.isEmpty(floatText) ? floatText : "+1")// 浮字体的内容
                .build();
        translateFloatingText.attach2Window(); //将FloatingText贴附在Window上
        //启动漂浮效果
        translateFloatingText.startFloating(view); // 传入一个View，FloatingText 就会相对于这个View执行漂浮效果
    }

    /**
     * 向上曲线效果
     *
     * @param activity
     * @param textContent
     * @param view
     */
    public static void curveFloating(Activity activity, String textContent, View view) {
        final FloatingText curveFloatingText = new FloatingText.FloatingTextBuilder(activity)
                .textColor(Color.RED)
                .textSize(60)
                .floatingAnimatorEffect(new CurvePathFloatingAnimator())
                .floatingPathEffect(new CurveFloatingPathEffect())
                .textContent(!TextUtils.isEmpty(textContent) ? textContent : "+1").build();
        curveFloatingText.attach2Window();
        curveFloatingText.startFloating(view);
    }

    /**
     * 垂直放大效果
     *
     * @param activity
     * @param floatText
     * @param view
     */
    public static void scaleFloatingText(Activity activity, String floatText, View view) {
        final FloatingText scaleFloatingText = new FloatingText.FloatingTextBuilder(activity)
                .textColor(Color.RED)
                .textSize(60)
                .offsetY(-100)
                .floatingAnimatorEffect(new ScaleFloatingAnimator())
                .textContent(!TextUtils.isEmpty(floatText) ? floatText : "+1").build();
        scaleFloatingText.attach2Window();
        scaleFloatingText.startFloating(view);
    }
}
