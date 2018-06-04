package com.zhanglin.signature.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 2016/8/29 13:37
 *
 * @author zhanglin
 * todo
 * 路径
 * <p/>
 */
public class MyPath extends Action {
    private Path path;

    private Paint paint;

    public MyPath(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
        path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
    }

    public void onDraw(Canvas canvas) {
        if (canvas == null) {
            return;
        }

        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        canvas.drawPath(path, paint);
    }

    public void onMove(float mx, float my) {
        path.lineTo(mx, my);
    }
}
