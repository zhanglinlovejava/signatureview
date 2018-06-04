package com.zhanglin.signature.doodle;

import android.graphics.Color;

import com.zhanglin.signature.doodle.action.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * 2016/8/29 13:31
 *
 * @author zhanglin
 * todo
 * 涂鸦板通道
 */
class DoodleChannel {
    /**
     * 当前所选的画笔
     */

    public Action action; // 当前的形状对象

    public int paintColor = Color.RED;

    public int paintSize = 5;


    /**
     * 记录所有形状的列表
     */
    public List<Action> actions = new ArrayList<>();


    /**
     * 设置当前画笔的颜色
     *
     * @param color
     */
    public void setColor(String color) {
        this.paintColor = Color.parseColor(color);
    }

    /**
     * 设置画笔的粗细
     *
     * @param size
     */
    public void setSize(int size) {
        if (size > 0) {
            this.paintSize = size;
        }
    }
}
