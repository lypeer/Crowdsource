package com.tesmple.crowdsource.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
