package com.tesmple.crowdsource.homework;

/**
 * Created by lypeer on 11/10/2015.
 */
public class Circle extends ShapeImpl {

    /**
     * 半径的string值
     */
    private int radius = 0;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        super.draw();
        if (getRadius() != 0) {
            System.out.println("画了一个半径为" + getRadius() + "的圆");
        }else {
            System.out.println("请初始化半径！");
        }
    }
}
