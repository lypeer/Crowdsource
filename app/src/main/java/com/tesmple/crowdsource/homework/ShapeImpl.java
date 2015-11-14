package com.tesmple.crowdsource.homework;

/**
 * Created by lypeer on 11/10/2015.
 */
public abstract class ShapeImpl implements Shape{

    /**
     * 绘制时的起始x值
     */
    private String fromX;

    /**
     * 绘制时的起始y值
     */
    private String fromY;

    @Override
    public String getFromX() {
        return fromX;
    }

    @Override
    public void setFromX(String fromX) {
        this.fromX = fromX;
    }

    @Override
    public String getFromY() {
        return fromY;
    }

    @Override
    public void setFromY(String fromY) {
        this.fromY = fromY;
    }

    @Override
    public void draw() {
        System.out.println("起始点的坐标为( " + getFromX() + " , " + getFromY() + " )");
    }
}
