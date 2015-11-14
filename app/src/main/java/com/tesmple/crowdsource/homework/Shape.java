package com.tesmple.crowdsource.homework;

/**
 * Created by lypeer on 11/10/2015.
 */
public interface Shape {

    /**
     * 设置绘制起始的x值
     * @param fromX 起始的x值
     */
    void setFromX(String fromX);

    /**
     * 获得绘制起始的x值
     * @return 起始的x值
     */
    String getFromX();

    /**
     * 设置绘制起始的y值
     * @param fromY 起始的x值
     */
    void setFromY(String fromY);

    /**
     * 获得绘制起始的y值
     * @return 起始的y值
     */
    String getFromY();

    /**
     * 绘制的函数
     */
    void draw();
}
