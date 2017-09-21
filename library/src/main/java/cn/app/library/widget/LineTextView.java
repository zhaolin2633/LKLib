package cn.app.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class LineTextView extends TextView {

    private Paint paint;

    public LineTextView(Context context) {
        super(context);
        initPaint();
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        canvas.drawRect(0, viewHeight / 2 - 1, viewWidth, viewHeight / 2 + 1, paint);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setColor(getCurrentTextColor());
        paint.setAntiAlias(true);
    }

}
