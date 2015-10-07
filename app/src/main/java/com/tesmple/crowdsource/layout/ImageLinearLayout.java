package com.tesmple.crowdsource.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.jar.Attributes;

/**
 * Created by ESIR on 2015/10/7.
 */
public class ImageLinearLayout extends LinearLayout {
    public ImageLinearLayout(Context context) {
        super(context);
    }

    public ImageLinearLayout(Context context,AttributeSet attrs){
        super(context , attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();
        int childHeightSize ;
        childHeightSize = (childWidthSize * 3/5);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,MeasureSpec.EXACTLY);
        //heightMeasureSpec = (int)((0.75)*((float)widthMeasureSpec));
        //heightMeasureSpec = widthMeasureSpec * 3/4;
        Log.i("widthpre",String.valueOf(widthMeasureSpec));
        Log.i("heightpre", String.valueOf(heightMeasureSpec));

        //heightMeasureSpec = widthMeasureSpec  * 3/4;
        Log.i("widthend",String.valueOf(widthMeasureSpec));
        Log.i("heightend", String.valueOf(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}