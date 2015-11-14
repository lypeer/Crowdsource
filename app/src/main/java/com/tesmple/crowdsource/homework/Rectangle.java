package com.tesmple.crowdsource.homework;

/**
 * Created by lypeer on 11/10/2015.
 */
public class Rectangle extends ShapeImpl {

    /**
     * 矩形的宽
     */
    private int width = 0;

    /**
     * 矩形的长
     */
    private int height = 0;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void draw() {
        super.draw();
        if(getWidth() == 0 || getHeight() == 0){
            System.out.println("请初始化长宽！");
        }else {
            System.out.println("画了一个长为" + getHeight() + ",宽为" + getWidth() + "的矩形");
        }
    }
}
