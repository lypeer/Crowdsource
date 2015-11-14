package com.tesmple.crowdsource.homework;

/**
 * Created by lypeer on 11/10/2015.
 */
public class Line extends ShapeImpl {

    /**
     * 绘制起始点的x坐标
     */
    private int fromLineX;

    /**
     * 绘制起始点的y坐标
     */
    private int fromLineY;

    /**
     * 绘制的终点的x坐标
     */
    private int toLineX;

    /**
     * 绘制的终点的y坐标
     */
    private int toLineY;

    public int getFromLineX() {
        return fromLineX;
    }

    public void setFromLineX(int fromLineX) {
        this.fromLineX = fromLineX;
    }

    public int getFromLineY() {
        return fromLineY;
    }

    public void setFromLineY(int fromLineY) {
        this.fromLineY = fromLineY;
    }

    public int getToLineX() {
        return toLineX;
    }

    public void setToLineX(int toLineX) {
        this.toLineX = toLineX;
    }

    public int getToLineY() {
        return toLineY;
    }

    public void setToLineY(int toLineY) {
        this.toLineY = toLineY;
    }

    @Override
    public void draw() {
        super.draw();
        if(getFromLineX() == 0 || getFromLineY() == 0 ||
                getToLineX() == 0 || getToLineY() == 0){

        }
    }
}
