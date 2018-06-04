package com.zhanglin.signature.doodle.action;

import android.graphics.Canvas;

/**
 * 2016/8/29 14:34
 *
 * @author zhanglin
 * todo
 * 形状基类，所有涂鸦板上的绘制的形状继承该基类
 */
public abstract class Action {
    protected float startX;
    protected float startY;
    protected float stopX;
    protected float stopY;
    protected int color;
    protected int size;

    Action(float startX, float startY, int color, int size) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = startX;
        this.stopY = startY;
        this.color = color;
        this.size = size;
    }

    /**
     * ACTION_MOVE事件触发时调用
     *
     * @param mx 当前MOVE到的手指位置x
     * @param my 当前MOVE到的手指位置y
     */
    public abstract void onMove(float mx, float my);

    /**
     * ACTION_MOVE过程或者ACTION_UP后形状的绘制
     *
     * @param canvas
     */
    public abstract void onDraw(Canvas canvas);
}