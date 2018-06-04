package com.zhanglin.signature.doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhanglin.signature.doodle.action.Action;
import com.zhanglin.signature.doodle.action.MyPath;

/**
 * 涂鸦板控件（基类）
 */
public class DoodleView extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "DoodleView";

    private SurfaceHolder surfaceHolder;

    private DoodleChannel paintChannel; // 绘图通道

    private int bgColor = Color.WHITE; // 背景颜色

    private float paintOffsetY = 0.0f; // 绘制时的Y偏移（去掉ActionBar,StatusBar,marginTop等高度）
    private float paintOffsetX = 0.0f; // 绘制事的X偏移（去掉marginLeft的宽度）

    private float lastX = 0.0f;
    private float lastY = 0.0f;

    public DoodleView(Context context) {
        super(context);
        init();
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        this.setFocusable(true);
        this.paintChannel = new DoodleChannel();
    }

    /**
     * 设置绘制时画笔的偏移
     *
     * @param x DoodleView的MarginLeft的宽度
     * @param y ActionBar与StatusBar及DoodleView的MarginTop的高度的和
     */
    public void setPaintOffset(float x, float y) {
        this.paintOffsetX = x;
        this.paintOffsetY = y;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        onPaintBackground();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceView created, width = " + width + ", height = " + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * ******************************* 绘图板 ****************************
     */

    /**
     * 设置绘制时的画笔颜色
     *
     * @param color
     */
    public void setPaintColor(String color) {
        this.paintChannel.setColor(color);
    }


    /**
     * 设置画笔的粗细
     *
     * @param size
     */
    public void setPaintSize(int size) {
        if (size > 0) {
            this.paintChannel.paintSize = size;
//            this.playbackChannel.paintSize = size;
        }
    }

    /**
     * 撤销一步
     *
     * @return 撤销是否成功
     */
    public synchronized boolean paintBack() {
        if (paintChannel == null) {
            return false;
        }

        boolean res = back();
        return res;
    }

    /**
     * 触摸绘图
     *
     * @param event
     * @return
     */
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        float touchX = event.getRawX();
        float touchY = event.getRawY();
        touchX -= paintOffsetX;
        touchY -= paintOffsetY;
        Log.i(TAG, "x=" + touchX + ", y=" + touchY);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onPaintActionStart(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                onPaintActionMove(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                onPaintActionEnd();
                break;
            default:
                break;
        }

        return true;
    }

    private void onPaintActionStart(float x, float y) {
        if (paintChannel == null) {
            return;
        }

        onActionStart(x, y);
    }

    private void onPaintActionMove(float x, float y) {
        if (paintChannel == null) {
            return;
        }

        if (!isNewPoint(x, y)) {
            return;
        }

        onActionMove(x, y);
    }

    private void onPaintActionEnd() {
        if (paintChannel == null) {
            return;
        }
        onActionEnd();
    }


    private void onPaintBackground() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(bgColor);  // 涂鸦板背景颜色
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void onActionStart(float x, float y) {
        paintChannel.action = new MyPath(x, y, paintChannel.paintColor, paintChannel.paintSize);
    }

    private void onActionMove(float x, float y) {

        if (paintChannel.action == null) {
            // 有可能action被清空，此时收到move，重新补个start
            onPaintActionStart(x, y);
        }
        Canvas canvas = surfaceHolder.lockCanvas();
        drawHistoryActions(canvas);
        // 绘制当前Action
        paintChannel.action.onMove(x, y);
        paintChannel.action.onDraw(canvas);
        if (canvas != null) {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void onActionEnd() {
        if (paintChannel.action == null) {
            return;
        }

        paintChannel.actions.add(paintChannel.action);
        paintChannel.action = null;
    }


    private void drawHistoryActions(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        // 绘制背景
        canvas.drawColor(bgColor);
        // 绘制所有历史Action
        if (paintChannel != null && paintChannel.actions != null) {
            for (Action a : paintChannel.actions) {
                a.onDraw(canvas);
            }
            // 绘制当前
            if (paintChannel.action != null) {
                paintChannel.action.onDraw(canvas);
            }
        }
    }

    private boolean back() {
        if (paintChannel.actions != null && paintChannel.actions.size() > 0) {
            paintChannel.actions.remove(paintChannel.actions.size() - 1);
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null) {
                return false;
            }
            drawHistoryActions(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
            return true;
        }
        return false;
    }

    public void clear() {
        if (paintChannel.actions != null) {
            paintChannel.actions.clear();
        }
        paintChannel.action = null;

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        drawHistoryActions(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private boolean isNewPoint(float x, float y) {
        if (Math.abs(x - lastX) <= 0.1f && Math.abs(y - lastY) <= 0.1f) {
            return false;
        }
        lastX = x;
        lastY = y;
        return true;
    }
}
